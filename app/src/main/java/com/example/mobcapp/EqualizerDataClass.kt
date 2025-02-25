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

        // Compare items list (count unique differences)
        val thisItems = this.items.orEmpty().toSet()
        val otherItems = other.items.orEmpty().toSet()
        val itemDifferences = (thisItems union otherItems) - (thisItems intersect otherItems)
        differenceCount += itemDifferences.size

        // Compare ingredients list (count unique differences)
        val thisIngredients = this.ingredient.orEmpty().toSet()
        val otherIngredients = other.ingredient.orEmpty().toSet()
        val ingredientDifferences = (thisIngredients union otherIngredients) - (thisIngredients intersect otherIngredients)
        differenceCount += ingredientDifferences.size

        return differenceCount
    }
}