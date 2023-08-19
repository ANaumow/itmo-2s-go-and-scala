package bank

import java.time.LocalDateTime

final case class Operation(
    time: LocalDateTime,
    amount: Int
)
