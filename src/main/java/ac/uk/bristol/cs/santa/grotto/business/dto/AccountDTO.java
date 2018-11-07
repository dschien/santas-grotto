package ac.uk.bristol.cs.santa.grotto.business.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountDTO {
    Long userId;

    String username;
    String password;
    String name;
    String role;
}
