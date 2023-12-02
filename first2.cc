#include <iostream>

int main() {
    std::string line;
    int64_t result = 0;
    std::unordered_map<std::string, int> numbers = {
            {"zero",  0},
            {"one",   1},
            {"two",   2},
            {"three", 3},
            {"four",  4},
            {"five",  5},
            {"six",   6},
            {"seven", 7},
            {"eight", 8},
            {"nine",  9},
            {"0",     0},
            {"1",     1},
            {"2",     2},
            {"3",     3},
            {"4",     4},
            {"5",     5},
            {"6",     6},
            {"7",     7},
            {"8",     8},
            {"9",     9},
    };

    while (std::getline(std::cin, line)) {
        std::string from_left = line;
        while (true) {
            for (const auto &[pattern, value]: numbers) {
                if (from_left.starts_with(pattern)) {
                    result += value * 10;
                    goto found_left;
                }
            }
            from_left = from_left.substr(1);
        }
        found_left:

        std::string from_right = line;
        while (true) {
            for (const auto &[pattern, value]: numbers) {
                if (from_right.ends_with(pattern)) {
                    result += value;
                    goto found_right;
                }
            }
            from_right = from_right.substr(0, from_right.length() - 1);
        }
        found_right:
    }
    std::cout << result << std::endl;
    return 0;
}