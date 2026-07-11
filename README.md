# Tutor AM Prem by Yor

Aplikasi Android sederhana dengan:
1. Login memakai kode 6 digit dari authenticator (TOTP standar, RFC 6238).
2. Halaman daftar video step-by-step, diputar langsung di dalam aplikasi (embed YouTube via WebView, tidak membuka app/browser YouTube).
3. Anti screenshot & screen recording (FLAG_SECURE) di semua halaman.
4. Sesi otomatis logout setelah 15 menit, harus masukkan kode ulang.

## Cara pakai kunci authenticator

Kunci rahasia yang dipakai adalah **AMPREM**. String ini sekaligus valid sebagai
secret Base32, jadi kamu (admin) tinggal masukkan **AMPREM** sebagai
"kunci setup manual" di aplikasi authenticator standar apa pun
(Google Authenticator, Authy, Microsoft Authenticator, dll) — pilih opsi
"masukkan kunci setup" / "enter key manually", isi nama akun bebas, dan
tempel AMPREM sebagai secret key. Aplikasi authenticator itu akan
menghasilkan kode 6 digit yang berganti tiap 30 detik, sama persis dengan
yang divalidasi aplikasi ini.

Kunci ini **tidak pernah ditampilkan** di dalam aplikasi Android-nya sendiri —
hanya dipakai di balik layar (`TotpUtil.kt`) untuk verifikasi.

## Mengganti video

Edit file `app/src/main/java/com/amprem/securevideo/Video.kt`, ganti nilai
`youtubeId` pada tiap `Video(...)` dengan ID video YouTube asli kamu
(bagian setelah `youtu.be/` atau parameter `v=` di URL YouTube).

## Mengubah durasi sesi

Edit `SessionManager.SESSION_DURATION_MS` di
`app/src/main/java/com/amprem/securevideo/SessionManager.kt`.

## Cara build

1. Buka folder ini di Android Studio (File > Open).
2. Tunggu Gradle sync selesai.
3. Run ke perangkat/emulator (tombol Run/▶).

## Catatan penting soal keamanan

- `FLAG_SECURE` mencegah screenshot & perekaman layar bawaan Android
  (juga menyembunyikan pratinjau di Recent Apps) pada mayoritas perangkat —
  ini praktik standar yang sama dipakai aplikasi seperti Netflix atau
  aplikasi perbankan. Namun ini bukan proteksi 100%: perangkat yang di-root
  atau kamera eksternal yang merekam layar tetap bisa menangkap tayangan.
- Karena video tetap dimuat dari YouTube (via embed player), video tersebut
  idealnya diset ke "Tidak Publik/Unlisted" di YouTube Studio, supaya tidak
  muncul di pencarian publik meski linknya tertanam di aplikasi.
