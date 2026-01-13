
package com.dit.interview.dto.res;

public class ApiResponse<T> {
    public boolean success;
    public T data;
    public String error;

    /**
     * Create success reponse
     * @param data, Response data
     * @return Response object
     * @param <T> any type of data
     */
    public static <T> ApiResponse<T> success(T data) {
        ApiResponse<T> r = new ApiResponse<>();
        r.success = true;
        r.data = data;
        return r;
    }


    /**
     * Create success reponse
     * @param msg, Error message
     * @return Response object
     * @param <T> any type of data
     */
    public static <T> ApiResponse<T> error(String msg) {
        ApiResponse<T> r = new ApiResponse<>();
        r.success = false;
        r.error = msg;
        return r;
    }
}
