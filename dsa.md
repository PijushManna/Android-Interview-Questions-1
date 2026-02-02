# DSA
### Check Palindromic number

```kotlin
/**
 * Checks if a string is a palindrome by:
 * - Ignoring numbers
 * - Ignoring non-alphanumeric characters
 * - Case-insensitive
 * - Considering only alphabetic characters (a–z, A–Z)
 */
fun isPalindrome(input: String): Boolean {
    var left = 0
    var right = input.length - 1

    while (left < right) {

        // Move left pointer until it points to a letter
        while (left < right && !input[left].isLetter()) {
            left++
        }

        // Move right pointer until it points to a letter
        while (left < right && !input[right].isLetter()) {
            right--
        }

        // Compare characters (case-insensitive)
        if (input[left].lowercaseChar() != input[right].lowercaseChar()) {
            return false
        }

        left++
        right--
    }

    return true
}

// Example usage
fun main() {
    println(isPalindrome("A man, a plan, a canal: Panama 123")) // true
    println(isPalindrome("race a car!")) // false
}
```
