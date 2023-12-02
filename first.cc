#include <fstream>
#include <iostream>

int main() {
    std::ifstream in("data/current.in");
    std::cin.rdbuf(in.rdbuf());

    std::string line;
    int64_t result = 0;
    while (std::getline(std::cin, line)) {
        for (char i : line) {
            if (i >= '0' && i <= '9') {
                result += (i - '0') * 10;
                break;
            }
        }
        for (int i = line.length() - 1; i >= 0; i--) {
            if (line[i] >= '0' && line[i] <= '9') {
                result += line[i] - '0';
                break;
            }
        }
    }
    std::cout << result << std::endl;
    return 0;
}