package com.skash.timetrack.core.exception

import java.lang.Exception

class MissingTimerActionException(override val message: String?): Exception()