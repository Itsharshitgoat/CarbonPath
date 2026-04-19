public class CarbonCalculator {
    public static double getEmissionFactor(String transport) {
        if (transport == null) return 0.0;

        switch (transport.toLowerCase()) {
            case "walking": return 0.0;
            case "bicycle": return 0.0;
            case "bike": return 0.05;
            case "car": return 0.12;
            case "bus": return 0.08;
            default: return 0.0;
        }
    }

    public static double calculateEmission(double distance, String transport) {
        return distance * getEmissionFactor(transport);
    }
}
