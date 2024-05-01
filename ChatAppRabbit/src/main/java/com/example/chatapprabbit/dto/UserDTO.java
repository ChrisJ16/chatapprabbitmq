package com.example.chatapprabbit.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.util.Objects;

@Getter
@Setter
public class UserDTO extends RepresentationModel<UserDTO> {

    private Integer id;
    private String username;
    private String password;

    public UserDTO(Integer id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserDTO)) return false;
        UserDTO userDTO = (UserDTO) o;
        return Objects.equals(getUsername(), userDTO.getUsername()) &&
                Objects.equals(getPassword(), userDTO.getPassword());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUsername(), getPassword());
    }
}
