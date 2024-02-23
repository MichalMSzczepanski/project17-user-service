package work.szczepanskimichal.service;

import org.apache.commons.lang3.RandomStringUtils;
import work.szczepanskimichal.enums.UserType;
import work.szczepanskimichal.model.User;
import work.szczepanskimichal.model.dto.UserCreateDto;

public abstract class UserAssembler {

    public static User assembleRandomUser() {
        return User.builder()
                .email(generateRandomEmail())
                .password(generateRandomPassword())
                .userType(UserType.USER)
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
                .userType(UserType.USER)
                .build();
    }

    private static String generateRandomEmail() {
        return RandomStringUtils.randomAlphanumeric(8) + "@example.com";
    }

    private static String generateRandomPassword() {
        return RandomStringUtils.randomAlphanumeric(10);
    }

    public static User hashUserPassword(User user, HashingService hashingService, String password) {
        return user.toBuilder()
                .password(hashingService.hashPassword(password))
                .build();
    }

    private static String generateRandomPhoneNumber() {
        return "+48" + RandomStringUtils.randomNumeric(9);
    }

}
