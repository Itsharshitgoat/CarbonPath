public class TestCoreLogic {
    public static void main(String[] args) {
        System.out.println("Starting tests...");
        assert CarbonCalculator.calculateEmission(10.0, "Car") == 1.2 : "10km Car should be 1.2";
        assert CarbonCalculator.calculateEmission(5.0, "Bus") == 0.4 : "5km Bus should be 0.4";
        assert CarbonCalculator.calculateEmission(2.0, "Bicycle") == 0.0 : "2km Bicycle should be 0.0";
        assert CarbonCalculator.calculateEmission(1.0, "Walking") == 0.0 : "1km Walking should be 0.0";

        assert SuggestionEngine.getSuggestedTransport(0.5).equals("Walking") : "0.5km should suggest Walking";
        assert SuggestionEngine.getSuggestedTransport(2.0).equals("Bicycle") : "2km should suggest Bicycle";
        assert SuggestionEngine.getSuggestedTransport(5.0).equals("Bus") : "5km should suggest Bus";
        assert SuggestionEngine.getSuggestedTransport(15.0).equals("Bike") : "15km should suggest Bike";

        System.out.println("All core logic tests passed.");
    }
}
