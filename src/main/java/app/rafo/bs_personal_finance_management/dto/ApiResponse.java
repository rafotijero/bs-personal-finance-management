package app.rafo.bs_personal_finance_management.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Generic API response for standardizing REST responses.
 * @param <T> The type of the response data
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private T data;          // The actual data returned by the API
    private String message;  // Message describing the result
    private int statusCode;  // HTTP status code
    private int count;       // Number of data elements (useful for lists)
}
