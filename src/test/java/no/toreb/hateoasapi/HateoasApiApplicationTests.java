package no.toreb.hateoasapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import no.toreb.hateoasapi.api.CustomMediaType;
import no.toreb.hateoasapi.api.common.response.CollectionResource;
import no.toreb.hateoasapi.api.common.response.HALResource;
import no.toreb.hateoasapi.api.v1.request.ItemRequest;
import no.toreb.hateoasapi.api.v1.request.UserRequest;
import no.toreb.hateoasapi.api.v1.response.ItemResponse;
import no.toreb.hateoasapi.api.v1.response.UserResponse;
import org.flywaydb.test.annotation.FlywayTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.IdGenerator;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
public class HateoasApiApplicationTests {

    @Autowired
    private TestRestTemplate template;

    @MockBean
    private IdGenerator idGenerator;

    @LocalServerPort
    private int port;

    @Autowired
    private ObjectMapper objectMapper;

    private ResponseDeserializer deserializer;

    @FlywayTest
    @Before
    public void setup() {
        deserializer = new ResponseDeserializer(objectMapper);

        template.getRestTemplate()
                .setInterceptors(List.of((request, body, execution) -> {
                    request.getHeaders().set(HttpHeaders.ACCEPT, CustomMediaType.V1_VALUE);

                    return execution.execute(request, body);
                }));
    }

    private Link createLink(final String path, final String rel) {
        return new Link("http://localhost:" + port + path, rel);
    }

    private <T> void assertResponseEntity(final ResponseEntity<T> response,
                                          final HttpStatus expectedStatus,
                                          final T expectedBody) {
        assertThat(response.getStatusCode()).isEqualTo(expectedStatus);
        assertThat(response.getBody()).isEqualTo(expectedBody);
        assertThat(response.getHeaders().get(HttpHeaders.CONTENT_TYPE).get(0)).startsWith(CustomMediaType.V1_VALUE);
    }

    @Test
    public void test() {
        // Post user
        final UserRequest userRequest = new UserRequest("ToreB");

        final UUID userId = UUID.randomUUID();
        when(idGenerator.generateId()).thenReturn(userId);

        final ResponseEntity<HALResource<UserResponse>> postUserEntity = deserializer.deserialize(
                UserResponse.class,
                () -> template.postForEntity("/users", new HttpEntity<>(userRequest), String.class));

        assertResponseEntity(postUserEntity, HttpStatus.CREATED, new HALResource<>(
                new UserResponse(userId, userRequest.getName()),
                List.of(createLink("/users/" + userId, "self"), createLink("/users", "users")),
                Map.of()));

        // GetById User
        final ResponseEntity<HALResource<UserResponse>> getUserByIdEntity = deserializer.deserialize(
                UserResponse.class, () -> template.getForEntity("/users/" + userId, String.class));
        assertResponseEntity(getUserByIdEntity, HttpStatus.OK, postUserEntity.getBody());

        // GetAll User
        final ResponseEntity<CollectionResource<UserResponse>> getAllUsers = deserializer.deserializeCollection(
                UserResponse.class, () -> template.getForEntity("/users", String.class));
        assertResponseEntity(getAllUsers, HttpStatus.OK, new CollectionResource<>("users",
                                                                                  List.of(getUserByIdEntity.getBody()),
                                                                                  List.of(createLink("/users",
                                                                                                     "self"))));

        // Post items
        final UUID itemId1 = UUID.randomUUID();
        final UUID itemId2 = UUID.randomUUID();
        when(idGenerator.generateId()).thenReturn(itemId1).thenReturn(itemId2);

        // Item 1
        final ItemRequest itemRequest1 = new ItemRequest(userId, "Item 1", "Just do it!");
        final ResponseEntity<HALResource<ItemResponse>> postItemEntity1 = deserializer.deserialize(
                ItemResponse.class, () -> template.postForEntity("/items", itemRequest1, String.class));
        assertResponseEntity(postItemEntity1, HttpStatus.CREATED, new HALResource<>(
                new ItemResponse(itemId1, userId, itemRequest1.getName(), itemRequest1.getDescription()),
                List.of(createLink("/items", "items"), createLink("/items/" + itemId1, "self")),
                Map.of("user", getUserByIdEntity.getBody())
        ));

        // Item 2
        final ItemRequest itemRequest2 = new ItemRequest(userId, "Item 2", "Do whenever...");
        final ResponseEntity<HALResource<ItemResponse>> postItemEntity2 = deserializer.deserialize(
                ItemResponse.class, () -> template.postForEntity("/items", itemRequest2, String.class));
        assertResponseEntity(postItemEntity2, HttpStatus.CREATED, new HALResource<>(
                new ItemResponse(itemId2, userId, itemRequest2.getName(), itemRequest2.getDescription()),
                List.of(createLink("/items", "items"), createLink("/items/" + itemId2, "self")),
                Map.of("user", getUserByIdEntity.getBody())
        ));

        // GetById Item
        final ResponseEntity<HALResource<ItemResponse>> getItemByIdEntity = deserializer.deserialize(
                ItemResponse.class, () -> template.getForEntity("/items/" + itemId1, String.class));
        assertResponseEntity(getItemByIdEntity, HttpStatus.OK, postItemEntity1.getBody());

        // GetAll Items
        final ResponseEntity<CollectionResource<ItemResponse>> getAllItems = deserializer.deserializeCollection(
                ItemResponse.class, () -> template.getForEntity("/items", String.class));
        assertResponseEntity(getAllItems, HttpStatus.OK, new CollectionResource<>(
                "items",
                List.of(postItemEntity1.getBody(), postItemEntity2.getBody()),
                List.of(createLink("/items", "self"))));

        // Update Item
        final ItemRequest itemRequestUpdate = new ItemRequest(userId, "Item 1!", "Just do it... please?");
        final ResponseEntity<HALResource<ItemResponse>> patchItem1Entity = deserializer.deserialize(
                ItemResponse.class,
                () -> template.exchange("/items/" + itemId1,
                                        HttpMethod.PATCH,
                                        new HttpEntity<>(itemRequestUpdate),
                                        String.class));
        assertResponseEntity(patchItem1Entity, HttpStatus.OK, new HALResource<>(
                new ItemResponse(itemId1, userId, itemRequestUpdate.getName(), itemRequestUpdate.getDescription()),
                List.of(createLink("/items", "items"), createLink("/items/" + itemId1, "self")),
                Map.of("user", getUserByIdEntity.getBody())
        ));

        // Delete item
        final ResponseEntity<HALResource<ItemResponse>> deleteItemEntity = deserializer.deserialize(
                ItemResponse.class,
                () -> template.exchange("/items/" + itemId2, HttpMethod.DELETE, null, String.class));
        assertResponseEntity(deleteItemEntity, HttpStatus.OK, new HALResource<>(null,
                                                                                List.of(createLink("/items", "items")),
                                                                                Map.of()));
    }
}

