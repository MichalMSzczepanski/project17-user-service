package work.szczepanskimichal.service;

import org.apache.commons.lang3.RandomStringUtils;
import work.szczepanskimichal.entity.User;
import work.szczepanskimichal.entity.UserCreateDto;
import work.szczepanskimichal.enums.Type;

public abstract class UserAssembler {

    public static User assembleRandomUser() {
        return User.builder()
                .email(generateRandomEmail())
                .password(generateRandomPassword())
                .type(Type.USER)
                .active(true)
                .phoneNumber(generateRandomPhoneNumber())
                .build();
    }

    public static UserCreateDto assembleRandomUserDto() {
        var password = generateRandomPassword();
        return UserCreateDto.builder()
                .email(generateRandomEmail())
                .password(password)
                .passwordConfirmation(password)
                .phoneNumber(generateRandomPhoneNumber())
                .type(Type.USER)
                .build();
    }

    private static String generateRandomEmail() {
        return RandomStringUtils.randomAlphanumeric(8) + "@example.com";
    }

    private static String generateRandomPassword() {
        return RandomStringUtils.randomAlphanumeric(10);
    }

    private static String generateRandomPhoneNumber() {
        return "+48" + RandomStringUtils.randomNumeric(9);
    }

}
