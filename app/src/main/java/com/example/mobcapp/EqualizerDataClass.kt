package com.example.mobcapp

data class EqualizerDataClass(
    val id: Int? = null,
    val items: List<Pair<String, Int>>? = emptyList(),
    val itemsCountMin: Int? = null,
    val itemsCountMax: Int? = null,
    val priceMin: Double? = null,
    val priceMax: Double? = null,
    val ingredient: List<Ingredient>? = null,
    val ingredientsCountMin: Int? = null,
    val ingredientsCountMax: Int? = null,
    val caloriesMin: Int? = null,
    val caloriesMax: Int? = null,
    val timeMin: Int? = null,
    val timeMax: Int? = null,
    val customerTimeLeftMin: Int? = null,
    val customerTimeLeftMax: Int? = null,
    val salesMin: Double? = null,
    val salesMax: Double? = null,
    val gramsMin: Double? = null,
    val gramsMax: Double? = null
) {
    fun compareWith(other: EqualizerDataClass): Int {
        var differenceCount = 0

        // Compare simple nullable properties
        val propertiesToCompare = listOf(
            itemsCountMin to other.itemsCountMin,
            itemsCountMax to other.itemsCountMax,
            priceMin to other.priceMin,
            priceMax to other.priceMax,
            ingredientsCountMin to other.ingredientsCountMin,
            ingredientsCountMax to other.ingredientsCountMax,
            caloriesMin to other.caloriesMin,
            caloriesMax to other.caloriesMax,
            timeMin to other.timeMin,
            timeMax to other.timeMax,
            customerTimeLeftMin to other.customerTimeLeftMin,
            customerTimeLeftMax to other.customerTimeLeftMax,
            salesMin to other.salesMin,
            salesMax to other.salesMax,
            gramsMin to other.gramsMin,
            gramsMax to other.gramsMax
        )

        for ((thisValue, otherValue) in propertiesToCompare) {
            if (thisValue != otherValue) differenceCount++
        }

        // Compare items list INCLUDING QUANTITIES!
        val thisItems = this.items.orEmpty().toMap()
        val otherItems = other.items.orEmpty().toMap()

        for ((item, quantity) in thisItems) {
            if (otherItems[item] != quantity) {
                differenceCount++ // Different quantity or missing item
            }
        }

        for ((item, quantity) in otherItems) {
            if (thisItems[item] != quantity) {
                differenceCount++ // Different quantity or missing item
            }
        }

        // Compare ingredients list (count unique differences)
        val thisIngredients = this.ingredient.orEmpty().toSet()
        val otherIngredients = other.ingredient.orEmpty().toSet()
        val ingredientDifferences = (thisIngredients union otherIngredients) - (thisIngredients intersect otherIngredients)
        differenceCount += ingredientDifferences.size

        return differenceCount
    }

}