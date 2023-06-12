package com.hotsix.iAmNotAlone.signup.domain;

import com.hotsix.iAmNotAlone.login.Role;
import com.hotsix.iAmNotAlone.signup.domain.dto.AddMemberDto;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;
    private String email;
    private String nickname;
    private String password;
    private LocalDate birth;
    private int gender;
    private String introduction;
    private String path;
    private String refreshToken;

    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToOne
    @JoinColumn(name = "region_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Region region;

    @Convert(converter = ListToStringConverter.class)
    private List<String> personalities = new ArrayList<>();

    public static Member of(AddMemberDto form, Region region, String password, String url) {
        return Member.builder()
            .email(form.getEmail())
            .nickname(form.getNickname())
            .password(password)
            .birth(form.getBirth())
            .gender(form.getGender())
            .introduction(form.getIntroduction())
            .path(url)
            .region(region)
            .personalities(form.getPersonalities())
            .role(Role.USER)
            .build();
    }

    public void updateRefreshToken(String refreshToken){
        this.refreshToken = refreshToken;
    }
    public void destroyRefreshToken(){
        this.refreshToken = null;
    }

    //추후삭제 메서드
    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
