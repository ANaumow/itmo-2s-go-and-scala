set
    запускаем файбер
    кладем в мапку
    добавляем колбек на завершение
        из мапки удаляем если есть

cancel
    если файбер запущен
        останавливаем
    если нет активного файбера
        устанавливаем на ноль



    Supervisor[F](await = false).use { supervisor =>
      supervisor.supervise(
        Console[F].println("start") >> Temporal[F].sleep(3.second) >> Console[F]
          .println("end")
      )
    }.void

здесь end не пишется, потому что await = false

