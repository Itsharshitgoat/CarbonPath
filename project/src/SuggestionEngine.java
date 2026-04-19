public class SuggestionEngine {
    public static String getSuggestion(double distance) {
        if (distance < 1.0) {
            return "Walking";
        } else if (distance >= 1.0 && distance <= 3.0) {
            return "Bicycle";
        } else if (distance > 3.0 && distance <= 10.0) {
            return "Bus";
        } else {
            return "Car/Bike (try to carpool or use public transport if available)";
        }
    }

    public static String getSuggestedTransport(double distance) {
        if (distance < 1.0) {
            return "Walking";
        } else if (distance >= 1.0 && distance <= 3.0) {
            return "Bicycle";
        } else if (distance > 3.0 && distance <= 10.0) {
            return "Bus";
        } else {
            return "Bus"; // Fix: Use Bus for > 10 km so calculateOptimalEmission matches
        }
    }

    public static double calculateOptimalEmission(double distance) {
        if (distance < 1.0) {
            return CarbonCalculator.calculateEmission(distance, "Walking");
        } else if (distance >= 1.0 && distance <= 3.0) {
            return CarbonCalculator.calculateEmission(distance, "Bicycle");
        } else if (distance > 3.0 && distance <= 10.0) {
            return CarbonCalculator.calculateEmission(distance, "Bus");
        } else {
            return CarbonCalculator.calculateEmission(distance, "Bus"); // Assuming Bus is the optimal for > 10 km
        }
    }

    public static double calculateSavings(double currentEmission, double optimalEmission) {
        double savings = currentEmission - optimalEmission;
        return savings > 0 ? savings : 0.0; // Savings can't be negative if user already uses optimal or better
    }
}
