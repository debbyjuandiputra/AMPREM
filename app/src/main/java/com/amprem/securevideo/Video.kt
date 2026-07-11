package com.amprem.securevideo

data class Video(
    val step: Int,
    val title: String,
    val description: String,
    val youtubeId: String // hanya ID video-nya, contoh dari https://youtu.be/dQw4w9WgXcQ -> "dQw4w9WgXcQ"
)

object VideoRepository {
    // TODO: Ganti youtubeId di bawah ini dengan ID video YouTube kamu yang sebenarnya.
    fun getVideos(): List<Video> = listOf(
        Video(1, "Langkah 1: Buat Akun", "Cara mendaftar akun di website", "dQw4w9WgXcQ"),
        Video(2, "Langkah 2: Pilih Paket", "Memilih paket premium yang sesuai", "dQw4w9WgXcQ"),
        Video(3, "Langkah 3: Metode Pembayaran", "Cara melakukan pembayaran", "dQw4w9WgXcQ"),
        Video(4, "Langkah 4: Konfirmasi", "Verifikasi dan aktivasi akun premium", "dQw4w9WgXcQ"),
        Video(5, "Langkah 5: Akses Fitur", "Cara mengakses fitur premium", "dQw4w9WgXcQ")
    )
}
