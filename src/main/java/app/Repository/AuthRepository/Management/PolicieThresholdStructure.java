package app.Repository.AuthRepository.Management;

import app.Auth.Flow.Services.PasswordService.PolicieBehaviour.TimePeriod;

public record PolicieThresholdStructure(
        int day,
        int week,
        int month,
        int year,
        int fiveYears,
        int tenYears
) {
    public int countFor(TimePeriod period) {
        return switch (period) {
            case DAY -> day;
            case WEEK -> week;
            case MONTH -> month;
            case YEAR -> year;
            case FIVE_YEARS -> fiveYears;
            case TEN_YEARS -> tenYears;
        };
    }

    public PolicieThresholdStructure includingAttempt() {
        return new PolicieThresholdStructure(
                day + 1,
                week + 1,
                month + 1,
                year + 1,
                fiveYears + 1,
                tenYears + 1
        );
    }
}
