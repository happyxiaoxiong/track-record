package cn.cnic.trackrecord.api.identity;


import cn.cnic.trackrecord.model.entity.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.authority.AuthorityUtils;

@Setter
@Getter
public class TokenUser extends org.springframework.security.core.userdetails.User {
    private User user;

    public TokenUser(User user) {
        super(user.getAccount(), user.getPassword(), AuthorityUtils.createAuthorityList(String.valueOf(user.getRole())));
        this.user = user;
    }
}
