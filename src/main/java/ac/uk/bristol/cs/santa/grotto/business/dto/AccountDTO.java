package ac.uk.bristol.cs.santa.grotto.business.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountDTO {
    Long userId;

    String username;
    String password;
    String name;
    String role;
}
