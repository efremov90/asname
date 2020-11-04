package org.asname.dto.requests;

import java.util.ArrayList;

public class GetRequestStatusHistoryResponseDTO {

    private ArrayList<RequestStatusHistoryDTO> Items;

    public GetRequestStatusHistoryResponseDTO() {
    }

    public ArrayList<RequestStatusHistoryDTO> getItems() {
        return Items;
    }

    public void setItems(ArrayList<RequestStatusHistoryDTO> items) {
        Items = items;
    }
}
