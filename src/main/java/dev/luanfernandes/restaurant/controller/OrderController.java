package dev.luanfernandes.restaurant.controller;

import static dev.luanfernandes.restaurant.common.constants.PathConstants.ORDERS_BY_ID_V1;
import static dev.luanfernandes.restaurant.common.constants.PathConstants.ORDERS_V1;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON_VALUE;

import dev.luanfernandes.restaurant.domain.request.OrderRequest;
import dev.luanfernandes.restaurant.domain.request.OrderUpdateQuantityRequest;
import dev.luanfernandes.restaurant.domain.response.OrderResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(produces = APPLICATION_JSON_VALUE)
public interface OrderController {

    @Operation(
            summary = "Create a new order",
            tags = {"orders"},
            responses = {
                @ApiResponse(responseCode = "202", description = "Accepted"),
                @ApiResponse(
                        responseCode = "400",
                        description = "Bad Request",
                        content =
                                @Content(
                                        mediaType = APPLICATION_PROBLEM_JSON_VALUE,
                                        schema = @Schema(implementation = ProblemDetail.class))),
                @ApiResponse(
                        responseCode = "404",
                        description = "Not Found",
                        content =
                                @Content(
                                        mediaType = APPLICATION_PROBLEM_JSON_VALUE,
                                        schema = @Schema(implementation = ProblemDetail.class))),
                @ApiResponse(
                        responseCode = "422",
                        description = "Unprocessable Entity",
                        content =
                                @Content(
                                        mediaType = APPLICATION_PROBLEM_JSON_VALUE,
                                        schema = @Schema(implementation = ProblemDetail.class))),
                @ApiResponse(
                        responseCode = "500",
                        description = "Internal Server Error",
                        content =
                                @Content(
                                        mediaType = APPLICATION_PROBLEM_JSON_VALUE,
                                        schema = @Schema(implementation = ProblemDetail.class)))
            })
    @PostMapping(value = ORDERS_V1, consumes = APPLICATION_JSON_VALUE)
    ResponseEntity<OrderResponse> create(@Valid @RequestBody OrderRequest request);

    @Operation(
            summary = "Get all orders",
            tags = {"orders"},
            responses = {
                @ApiResponse(responseCode = "200", description = "OK"),
                @ApiResponse(
                        responseCode = "400",
                        description = "Bad Request",
                        content =
                                @Content(
                                        mediaType = APPLICATION_PROBLEM_JSON_VALUE,
                                        schema = @Schema(implementation = ProblemDetail.class))),
                @ApiResponse(
                        responseCode = "404",
                        description = "Not Found",
                        content =
                                @Content(
                                        mediaType = APPLICATION_PROBLEM_JSON_VALUE,
                                        schema = @Schema(implementation = ProblemDetail.class))),
                @ApiResponse(
                        responseCode = "422",
                        description = "Unprocessable Entity",
                        content =
                                @Content(
                                        mediaType = APPLICATION_PROBLEM_JSON_VALUE,
                                        schema = @Schema(implementation = ProblemDetail.class))),
                @ApiResponse(
                        responseCode = "500",
                        description = "Internal Server Error",
                        content =
                                @Content(
                                        mediaType = APPLICATION_PROBLEM_JSON_VALUE,
                                        schema = @Schema(implementation = ProblemDetail.class)))
            })
    @GetMapping(value = ORDERS_V1)
    ResponseEntity<List<OrderResponse>> getAll();

    @Operation(
            summary = "Get a order by id",
            tags = {"orders"},
            responses = {
                @ApiResponse(responseCode = "200", description = "OK"),
                @ApiResponse(
                        responseCode = "400",
                        description = "Bad Request",
                        content =
                                @Content(
                                        mediaType = APPLICATION_PROBLEM_JSON_VALUE,
                                        schema = @Schema(implementation = ProblemDetail.class))),
                @ApiResponse(
                        responseCode = "404",
                        description = "Not Found",
                        content =
                                @Content(
                                        mediaType = APPLICATION_PROBLEM_JSON_VALUE,
                                        schema = @Schema(implementation = ProblemDetail.class))),
                @ApiResponse(
                        responseCode = "422",
                        description = "Unprocessable Entity",
                        content =
                                @Content(
                                        mediaType = APPLICATION_PROBLEM_JSON_VALUE,
                                        schema = @Schema(implementation = ProblemDetail.class))),
                @ApiResponse(
                        responseCode = "500",
                        description = "Internal Server Error",
                        content =
                                @Content(
                                        mediaType = APPLICATION_PROBLEM_JSON_VALUE,
                                        schema = @Schema(implementation = ProblemDetail.class)))
            })
    @GetMapping(value = ORDERS_BY_ID_V1)
    ResponseEntity<OrderResponse> getById(@PathVariable Long id);

    @Operation(
            summary = "Update quantity of a product in order",
            tags = {"orders"},
            responses = {
                @ApiResponse(responseCode = "202", description = "Accepted"),
                @ApiResponse(
                        responseCode = "400",
                        description = "Bad Request",
                        content =
                                @Content(
                                        mediaType = APPLICATION_PROBLEM_JSON_VALUE,
                                        schema = @Schema(implementation = ProblemDetail.class))),
                @ApiResponse(
                        responseCode = "404",
                        description = "Not Found",
                        content =
                                @Content(
                                        mediaType = APPLICATION_PROBLEM_JSON_VALUE,
                                        schema = @Schema(implementation = ProblemDetail.class))),
                @ApiResponse(
                        responseCode = "422",
                        description = "Unprocessable Entity",
                        content =
                                @Content(
                                        mediaType = APPLICATION_PROBLEM_JSON_VALUE,
                                        schema = @Schema(implementation = ProblemDetail.class))),
                @ApiResponse(
                        responseCode = "500",
                        description = "Internal Server Error",
                        content =
                                @Content(
                                        mediaType = APPLICATION_PROBLEM_JSON_VALUE,
                                        schema = @Schema(implementation = ProblemDetail.class)))
            })
    @PutMapping(value = ORDERS_BY_ID_V1)
    ResponseEntity<Object> updateProductQuantityInOrder(
            @PathVariable Long id, @Valid @RequestBody OrderUpdateQuantityRequest request);
}
