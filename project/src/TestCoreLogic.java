public class TestCoreLogic {
    public static void main(String[] args) {
        System.out.println("Starting tests...");
        assert CarbonCalculator.calculateEmission(10.0, "Car") == 1.2 : "10km Car should be 1.2";
        System.out.println("All core logic tests passed.");
    }
}
