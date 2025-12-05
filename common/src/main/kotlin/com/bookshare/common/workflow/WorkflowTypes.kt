package com.bookshare.common.workflow

typealias Workflow<Command, Result> = (Command) -> Result

typealias SuspendWorkflow<Command, Result> = suspend (Command) -> Result
