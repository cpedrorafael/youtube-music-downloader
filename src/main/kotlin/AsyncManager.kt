package com.guayaba.youtubeMusicDownload

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Semaphore

class AsyncManager {
    companion object {
        val scope = CoroutineScope(Dispatchers.Default)
        val semaphore = Semaphore(6) //Limit downloads to 6 concurrently
    }
}