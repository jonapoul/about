package com.jonapoul.logs.domain

import androidx.activity.result.contract.ActivityResultContracts

internal class CreateZipDocument :
    ActivityResultContracts.CreateDocument(mimeType = "application/zip")
