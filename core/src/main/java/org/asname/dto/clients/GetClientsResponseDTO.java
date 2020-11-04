package org.asname.dto.clients;

import org.asname.dto.clients.ClientDTO;

import java.util.ArrayList;

public class GetClientsResponseDTO {

    private ArrayList<ClientDTO> Items;

    public GetClientsResponseDTO() {
    }

    public GetClientsResponseDTO(ArrayList<ClientDTO> items) {
        Items = items;
    }

    public ArrayList<ClientDTO> getItems() {
        return Items;
    }

    public void setItems(ArrayList<ClientDTO> items) {
        Items = items;
    }
}
