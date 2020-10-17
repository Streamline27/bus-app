package lv.proofit.busapp.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lv.proofit.busapp.api.draft.price.request.DraftPriceRequest;
import lv.proofit.busapp.api.draft.price.response.DraftPriceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;

import java.io.UnsupportedEncodingException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@Component
public class TestApiClient {

    private final MockMvc mockMvc;
    private final ObjectMapper om;

    @Autowired
    public TestApiClient(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
        this.om = new ObjectMapper();
    }

    public TestApiResult<DraftPriceResponse> calculateDraftPrice(DraftPriceRequest request) throws Exception {
        return executePost("/draft-price", request, DraftPriceResponse.class);
    }

    private <T> TestApiResult<T> executePost(String path, Object requestBody, Class<T> responseClass) throws Exception {
        MockHttpServletResponse result = mockMvc.perform(post(path)
                .contentType("application/json")
                .content(toJson(requestBody)))
                .andReturn()
                .getResponse();
        return new TestApiResult<>(fromJson(result, responseClass), result.getStatus());
    }

    private <T> T fromJson(MockHttpServletResponse response, Class<T> tClass) {
        try {
            return om.readValue(response.getContentAsString(), tClass);
        } catch (JsonProcessingException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    private <T> String toJson(T object) {
        try {
            return om.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
