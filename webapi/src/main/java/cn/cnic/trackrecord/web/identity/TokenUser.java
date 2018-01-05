package cn.cnic.trackrecord.web.identity;


import cn.cnic.trackrecord.data.entity.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.authority.AuthorityUtils;

@Setter
@Getter
public class TokenUser extends org.springframework.security.core.userdetails.User {
    private User user;

    public TokenUser(User user) {
        super(user.getAccount(), user.getPassword(), AuthorityUtils.createAuthorityList(user.getRole().getName()));
        this.user = user;
    }
}
