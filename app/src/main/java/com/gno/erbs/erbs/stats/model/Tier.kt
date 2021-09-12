package com.gno.erbs.erbs.stats.model

import timber.log.Timber

enum class Tier(val title: String, val value: Int) {

    IMMORTAL_1("Immortal 1", 3100),
    IMMORTAL_2("Immortal 2", 3000),
    IMMORTAL_3("Immortal 3", 2900),
    IMMORTAL_4("Immortal 4", 2800),

    TITAN_1("Titan 1", 2700),
    TITAN_2("Titan 2", 2600),
    TITAN_3("Titan 3", 2500),
    TITAN_4("Titan 4", 2400),

    DIAMOND_1("Diamond 1", 2300),
    DIAMOND_2("Diamond 2", 2200),
    DIAMOND_3("Diamond 3", 2100),
    DIAMOND_4("Diamond 4", 2000),

    PLATINUM_1("Platinum 1", 1900),
    PLATINUM_2("Platinum 2", 1800),
    PLATINUM_3("Platinum 3", 1700),
    PLATINUM_4("Platinum 4", 1600),

    GOLD_1("Gold 1", 1500),
    GOLD_3("Gold 3", 1300),
    GOLD_4("Gold 4", 1200),

    SILVER_1("Silver 1", 1100),
    SILVER_2("Silver 2", 1000),
    SILVER_3("Silver 3", 900),
    SILVER_4("Silver 4", 800),

    BRONZE_1("Bronze 1", 700),
    BRONZE_2("Bronze 2", 600),
    BRONZE_3("Bronze 3", 500),
    BRONZE_4("Bronze 4", 400),

    IRON_1("Iron 1", 300),
    IRON_2("Iron 2", 200),
    IRON_3("Iron 3", 100),
    IRON_4("Iron 4", 0);

    companion object {
        fun findByMmr(mmr: Int): Tier {
            val value = mmr / 100 * 100
            var result = IRON_4
            try {
                result = if (value >= 3100) IMMORTAL_1 else values().first { it.value == value }
            } catch (e: Exception) {
                Timber.e(e)
            }
            return result
        }
    }
}