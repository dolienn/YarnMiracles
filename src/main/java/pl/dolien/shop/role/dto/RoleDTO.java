package pl.dolien.shop.role.dto;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class RoleDTO implements Serializable {
    private Integer id;
    private String name;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
}
