# Design Document (DESIGN.md): Aplikasi Profil Interaktif Kelompok 3

## 1. Visi Desain & Arah Estetika ("Playful Daylight / Fun & Light")

Desain aplikasi **Kelompok 3** mengusung konsep **"Playful Daylight"** — perpaduan antarmuka bersih bertema terang (*Light Mode*) dengan elemen tiga dimensi bergaya *soft clay/toy*, warna-warna cerah yang segar, sudut membulat (*squircle*), serta animasi pegas (*spring physics*) yang membal saat disentuh. Pendekatan ini membuat halaman perkenalan kelompok terasa hidup, ramah, dan berkesan saat dipresentasikan.

### Prinsip Utama UI/UX
- **Light & Airy Surface**: Latar belakang menggunakan warna krem hangat dan putih bersih (`#FFFDF7` dan `#FFFFFF`) agar warna aksen tiap anggota kelompok dan objek 3D tampil menonjol.
- **Tactile & Bouncy**: Setiap kartu nama pada bagan memiliki bayangan lembut (*soft elevation* atau *neo-brutalist pastel border* tipis) dan memberikan respons membal (*scale spring*) ketika diklik.
- **Progressive Disclosure**: Halaman utama tetap ringkas karena hanya menampilkan judul **Kelompok 3**, **Animasi 3D**, dan **Bagan Nama Anggota**. Data detail baru dimunculkan secara terfokus saat salah satu anggota pada bagan diklik.

## 2. Sistem Warna (Color Palette)

Aplikasi dikunci pada skema warna terang (*Light Color Scheme*) untuk menjaga nuansa ceria dan konsistensi visual.

| Peran Warna | Nama Token | Kode Hex | Penggunaan pada UI |
| :--- | :--- | :--- | :--- |
| **Background Utama** | `WarmCreamBg` | `#FFFDF9` | Latar belakang utama `Scaffold` halaman pertama |
| **Surface Kartu / Sheet** | `PureWhiteSurface` | `#FFFFFF` | Latar belakang node bagan dan *Modal Bottom Sheet* |
| **Surface Sekunder** | `SoftCloudSurface` | `#F3F6FF` | Area panggung (*stage*) di belakang objek animasi 3D |
| **Primary (Brand Kelompok 3)** | `ElectricSkyBlue` | `#3B82F6` | Badge utama "Kelompok 3", tombol aksi utama, dan fokus aktif |
| **Secondary Accent** | `SunnyMango` | `#FFB703` | Sorotan Ketua Kelompok, ikon bintang, dan aksen dekoratif |
| **Tertiary Accent** | `BubblegumCoral` | `#FF6B8B` | Aksen interaktif, *badge* hobi, dan indikator sentuh |
| **Garis Konektor Bagan** | `ChartLineSlate` | `#CBD5E1` | Garis penghubung antar-node pada bagan organisasi |
| **Teks Utama** | `DeepInkNavy` | `#1E293B` | Judul, nama anggota, dan isi biodata (kontras tinggi) |
| **Teks Sekunder** | `MutedSlate` | `#64748B` | Label NIM, nama atribut, dan subjudul |

### Warna Aksen Individual Anggota (Node Palette)
Agar bagan terlihat *fun* dan mudah dibedakan, setiap node anggota diberi warna aksen pastel lembut:
- **Anggota 1 (Ketua)**: `#FFF3C4` (Butter Yellow) dengan border `#F59E0B`
- **Anggota 2**: `#E0F2FE` (Sky Pastel) dengan border `#38BDF8`
- **Anggota 3**: `#FCE7F3` (Cotton Candy Pink) dengan border `#F472B6`
- **Anggota 4**: `#DCFCE7` (Mint Sorbet) dengan border `#4ADE80`
- **Anggota 5**: `#F3E8FF` (Lavender Pop) dengan border `#C084FC`

## 3. Tipografi (Typography Scale)

Menggunakan kombinasi font **Fredoka** (atau **Plus Jakarta Sans ExtraBold**) untuk judul yang bulat dan ramah, dipadukan dengan **Plus Jakarta Sans** untuk keterbacaan data biodata.

| Gaya Teks | Ukuran / Line Height | Bobot (Weight) | Penggunaan |
| :--- | :--- | :--- | :--- |
| `displayMedium` | `32.sp` / `40.sp` | ExtraBold (`W800`) | Judul utama **"Kelompok 3"** di halaman pertama |
| `titleLarge` | `22.sp` / `28.sp` | Bold (`W700`) | Nama lengkap anggota pada tampilan detail |
| `titleMedium` | `16.sp` / `22.sp` | Bold (`W700`) | Nama anggota di dalam kotak/node bagan |
| `bodyMedium` | `14.sp` / `20.sp` | Medium (`W500`) | Isi data profil (Jurusan, Hobi, Quote, Fun Fact) |
| `labelMedium` | `12.sp` / `16.sp` | SemiBold (`W600`) | Badge peran (Ketua/Anggota), chip NIM, dan petunjuk klik |

## 4. Tata Letak Layar & Wireframe Struktural

### 4.1 Halaman Utama (Home Screen)
Halaman utama disusun secara vertikal menggunakan `LazyColumn` atau `Column` dengan `verticalScroll` yang terbagi menjadi 3 zona visual jelas:

```text
+---------------------------------------------------+
|  [ ✨ TUGAS MOBILE PROGRAMMING ]                  |
|                 KELOMPOK 3                        |  <-- Zona 1: Header Identitas
|        "Solid, Kreatif, & Inovatif!"              |
+---------------------------------------------------+
|                                                   |
|         .---------------------------.             |
|        /     [ OBJEK 3D INTERAKTIF ] \            |  <-- Zona 2: Panggung Animasi 3D
|       |    (Maskot / Kubus 3D "3")    |           |      (Bisa diputar / disentuh)
|        \   "Sentuh & putar aku! 👆"  /            |
|         '---------------------------'             |
|                                                   |
+---------------------------------------------------+
|  STRUKTUR ANGGOTA (Klik nama untuk detail)        |
|                                                   |
|              +-------------------+                |
|              |  👑 [Avatar]      |                |
|              |  Nama Ketua       |                |
|              |  (Ketua Kelompok) |                |
|              +---------+---------+                |
|                        |                          |
|          +-------------+-------------+            |  <-- Zona 3: Bagan Interaktif
|          |                           |            |      (Klik Node -> Buka Data)
|  +-------+-------+           +-------+-------+    |
|  | 🎨 [Avatar]   |           | 💻 [Avatar]   |    |
|  | Nama Anggota 2|           | Nama Anggota 3|    |
|  +-------+-------+           +-------+-------+    |
|          |                           |            |
|  +-------+-------+           +-------+-------+    |
|  | 🚀 [Avatar]   |           | ✨ [Avatar]   |    |
|  | Nama Anggota 4|           | Nama Anggota 5|    |
|  +---------------+           +---------------+    |
+---------------------------------------------------+
```

### 4.2 Tampilan Detail Anggota (Modal Bottom Sheet / Detail Card)
Ketika salah satu node nama anggota pada bagan diklik, `ModalBottomSheet` (Material 3) muncul dengan animasi pegas dari bawah layar:

```text
+---------------------------------------------------+
|                       ===                         |  <-- Drag Handle
|            +-----------------------+              |
|            |   [ FOTO / AVATAR ]   |              |
|            +-----------------------+              |
|                 NAMA LENGKAP                      |
|           [ Badge: Peran Kelompok ]               |
|                                                   |
|  +---------------------------------------------+  |
|  | 🪪 NIM          : [Nomor Induk Mahasiswa]   |  |
|  | 🎓 Program Studi: [Nama Prodi / Kelas]      |  |
|  | 🎮 Hobi & Minat : [Daftar Hobi Anggota]     |  |
|  | 💡 Fun Fact     : [Fakta Unik Anggota]      |  |
|  +---------------------------------------------+  |
|                                                   |
|  "Quote atau motto singkat dari anggota..."       |
|                                                   |
|  [ 📸 Instagram ]               [ 💻 GitHub ]     |
|                                                   |
|             ( Tutup Detail Profil )               |
+---------------------------------------------------+
```

## 5. Spesifikasi Komponen & Animasi Jetpack Compose

### 5.1 Komponen Animasi 3D (`Hero3DSection`)
Untuk menghadirkan pengalaman 3D yang ringan, interaktif, dan mudah dijalankan di semua perangkat/emulator Android, desain ini mendukung dua pendekatan terintegrasi di Compose:

1. **Pendekatan Native 3D Scene (`Sceneview` Filament)**:
   - Memuat file `kelompok3_mascot.glb` dari folder `src/main/assets/models/`.
   - Menyediakan animasi *auto-rotate* lembut pada sumbu Y dan memungkinkan pengguna memutar objek 3D secara bebas menggunakan gestur `detectDragGestures`.
2. **Pendekatan Pure Compose 3D Interactive Cube/Card (`graphicsLayer` + `Spring`)**:
   - Menggunakan `Animatable` untuk `rotationX`, `rotationY`, dan `translationY` (efek melayang naik-turun menggunakan `rememberInfiniteTransition`).
   - Saat pengguna menggeser jari di atas area 3D, objek berputar mengikuti arah jari secara *real-time* dan membal kembali ke posisi tengah menggunakan `spring(dampingRatio = Spring.DampingRatioMediumBouncy)` saat jari dilepas.

### 5.2 Bagan Struktur Organisasi (`OrgChartSection`)
- **Layout Node**: Menggunakan `Column` dan `Row` terpusat untuk menempatkan kartu Ketua di baris pertama, diikuti baris anggota di bawahnya.
- **Garis Konektor (`Canvas` Overlay/Background)**:
  - Menggunakan koordinat `onGloballyPositioned` dari setiap kartu anggota untuk menghitung titik tengah bawah (`bottomCenter`) node induk dan titik tengah atas (`topCenter`) node anak.
  - Menggambar garis lengkung halus (*cubic bezier path*) berwarna `#CBD5E1` dengan ketebalan `3.dp` dan titik bulatan kecil di setiap ujung sambungan.
- **Interaksi Klik Node (`MemberNodeCard`)**:
  - Menggunakan `MutableInteractionSource` dan `collectIsPressedAsState()` untuk mengecilkan skala kartu menjadi `0.94f` saat ditekan, lalu membal ke `1.0f` dan memicu callback `onMemberClick(member)`.

## 6. Contoh Implementasi Kode UI (Jetpack Compose Blueprint)

Berikut adalah rancangan kode struktur utama di Jetpack Compose yang menggabungkan Header Kelompok 3, Animasi 3D, Bagan Nama Kelompok, dan Popup Detail ketika diklik:

```kotlin
package com.kelompok3.profileapp.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kelompok3.profileapp.model.MemberProfile
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Kelompok3HomeScreen(
    members: List<MemberProfile>
) {
    var selectedMember by remember { mutableStateOf<MemberProfile?>(null) }
    val scrollState = rememberScrollState()

    Scaffold(
        containerColor = Color(0xFFFFFDF9) // WarmCreamBg (Fun & Light)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 1. HEADER KELOMPOK 3
            Surface(
                color = Color(0xFFE0F2FE),
                shape = RoundedCornerShape(50),
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                Text(
                    text = "✨ TIM PENGEMBANG ANDROID ✨",
                    color = Color(0xFF0284C7),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                )
            }

            Text(
                text = "Kelompok 3",
                fontSize = 34.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF1E293B)
            )

            Text(
                text = "Kenali tim kami! Geser objek 3D & klik bagan nama di bawah 👇",
                fontSize = 14.sp,
                color = Color(0xFF64748B),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 4.dp, bottom = 20.dp)
            )

            // 2. KOMPONEN ANIMASI 3D INTERAKTIF
            Interactive3DHeroBadge()

            Spacer(modifier = Modifier.height(28.dp))

            // 3. BAGAN NAMA ANGGOTA KELOMPOK (KLIK UNTUK LIHAT DATA)
            Text(
                text = "Bagan Anggota Kelompok 3",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E293B),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            InteractiveOrgChart(
                members = members,
                onMemberClick = { member -> selectedMember = member }
            )
        }

        // 4. MODAL DETAIL DATA ANGGOTA SAAT BAGAN DIKLIK
        selectedMember?.let { member ->
            ModalBottomSheet(
                onDismissRequest = { selectedMember = null },
                containerColor = Color.White,
                shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
            ) {
                MemberDetailContent(
                    member = member,
                    onClose = { selectedMember = null }
                )
            }
        }
    }
}

@Composable
fun Interactive3DHeroBadge() {
    val scope = rememberCoroutineScope()
    val rotationX = remember { Animatable(12f) }
    val rotationY = remember { Animatable(-15f) }

    // Animasi melayang (floating) otomatis
    val infiniteTransition = rememberInfiniteTransition(label = "float3d")
    val floatOffsetY by infiniteTransition.animateFloat(
        initialValue = -10f,
        targetValue = 10f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "floatY"
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .height(210.dp)
            .clip(RoundedCornerShape(28.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFFEFF6FF), Color(0xFFFFF7ED))
                )
            )
            .border(2.dp, Color(0xFFDBEAFE), RoundedCornerShape(28.dp))
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragEnd = {
                        scope.launch {
                            rotationX.animateTo(12f, spring(dampingRatio = Spring.DampingRatioMediumBouncy))
                        }
                        scope.launch {
                            rotationY.animateTo(-15f, spring(dampingRatio = Spring.DampingRatioMediumBouncy))
                        }
                    }
                ) { change, dragAmount ->
                    change.consume()
                    scope.launch {
                        rotationY.snapTo((rotationY.value + dragAmount.x * 0.4f).coerceIn(-55f, 55f))
                        rotationX.snapTo((rotationX.value - dragAmount.y * 0.4f).coerceIn(-45f, 45f))
                    }
                }
            }
    ) {
        // Objek 3D Bertekstur Berlapis dengan Camera Distance & Perspektif
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(135.dp)
                .graphicsLayer {
                    this.translationY = floatOffsetY
                    this.rotationX = rotationX.value
                    this.rotationY = rotationY.value
                    this.cameraDistance = 12f * density
                }
                .shadow(20.dp, RoundedCornerShape(32.dp), spotColor = Color(0xFF3B82F6))
                .clip(RoundedCornerShape(32.dp))
                .background(
                    Brush.linearGradient(
                        colors = listOf(Color(0xFF3B82F6), Color(0xFF8B5CF6), Color(0xFFEC4899))
                    )
                )
                .border(4.dp, Color.White, RoundedCornerShape(32.dp))
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "3D",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFFFFE4E6)
                )
                Text(
                    text = "K-3",
                    fontSize = 42.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White
                )
                Text(
                    text = "PUTAR AKU!",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White.copy(alpha = 0.9f)
                )
            }
        }
    }
}

@Composable
fun InteractiveOrgChart(
    members: List<MemberProfile>,
    onMemberClick: (MemberProfile) -> Unit
) {
    val leader = members.firstOrNull { it.isLeader } ?: members.firstOrNull()
    val teamMembers = members.filter { it.id != leader?.id }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        // Node Puncak: Ketua Kelompok
        leader?.let {
            MemberNodeCard(
                member = it,
                onClick = { onMemberClick(it) },
                modifier = Modifier.width(210.dp)
            )
        }

        // Garis Konektor Bagan (Dari Ketua ke Cabang Anggota)
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(44.dp)
        ) {
            val centerX = size.width / 2f
            val leftBranchX = size.width * 0.25f
            val rightBranchX = size.width * 0.75f
            val midY = size.height / 2f

            val path = Path().apply {
                // Garis vertikal dari Ketua
                moveTo(centerX, 0f)
                lineTo(centerX, midY)
                // Garis horizontal cabang
                moveTo(leftBranchX, midY)
                lineTo(rightBranchX, midY)
                // Garis turun ke kolom kiri & kanan
                moveTo(leftBranchX, midY)
                lineTo(leftBranchX, size.height)
                moveTo(rightBranchX, midY)
                lineTo(rightBranchX, size.height)
            }
            drawPath(
                path = path,
                color = Color(0xFF94A3B8),
                style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
            )
        }

        // Grid Baris Anggota Kelompok
        val chunkedMembers = teamMembers.chunked(2)
        chunkedMembers.forEachIndexed { index, rowMembers ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                rowMembers.forEach { member ->
                    MemberNodeCard(
                        member = member,
                        onClick = { onMemberClick(member) },
                        modifier = Modifier.weight(1f)
                    )
                }
                if (rowMembers.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
            if (index < chunkedMembers.lastIndex) {
                // Garis vertikal penghubung ke baris berikutnya
                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(24.dp)
                ) {
                    val leftX = size.width * 0.25f
                    val rightX = size.width * 0.75f
                    drawLine(
                        color = Color(0xFF94A3B8),
                        start = Offset(leftX, 0f),
                        end = Offset(leftX, size.height),
                        strokeWidth = 3.dp.toPx()
                    )
                    drawLine(
                        color = Color(0xFF94A3B8),
                        start = Offset(rightX, 0f),
                        end = Offset(rightX, size.height),
                        strokeWidth = 3.dp.toPx()
                    )
                }
            }
        }
    }
}

@Composable
fun MemberNodeCard(
    member: MemberProfile,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = Color(member.accentColorHex),
        shape = RoundedCornerShape(20.dp),
        shadowElevation = 4.dp,
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .clickable { onClick() }
            .border(2.dp, Color(0xFF1E293B).copy(alpha = 0.12f), RoundedCornerShape(20.dp))
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(14.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(Color.White)
            ) {
                Text(
                    text = member.nickName.take(2).uppercase(),
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF1E293B)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = member.nickName,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = Color(0xFF1E293B),
                textAlign = TextAlign.Center
            )
            Text(
                text = member.role,
                fontSize = 12.sp,
                color = Color(0xFF475569),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(6.dp))
            Surface(
                color = Color.White.copy(alpha = 0.75f),
                shape = RoundedCornerShape(50)
            ) {
                Text(
                    text = "Klik detail →",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF2563EB),
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                )
            }
        }
    }
}

@Composable
fun MemberDetailContent(
    member: MemberProfile,
    onClose: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(Color(member.accentColorHex))
                .border(3.dp, Color(0xFF3B82F6), CircleShape)
        ) {
            Text(
                text = member.nickName.take(2).uppercase(),
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF1E293B)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = member.fullName,
            fontSize = 22.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF1E293B),
            textAlign = TextAlign.Center
        )

        Surface(
            color = Color(0xFFDBEAFE),
            shape = RoundedCornerShape(50),
            modifier = Modifier.padding(top = 6.dp, bottom = 16.dp)
        ) {
            Text(
                text = member.role,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1D4Ed8),
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 4.dp)
            )
        }

        // Tabel Kartu Biodata Anggota
        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                DetailRow(label = "🪪 NIM", value = member.nim)
                DetailRow(label = "🎓 Prodi", value = member.major)
                DetailRow(label = "🎮 Hobi", value = member.hobby)
                DetailRow(label = "💡 Fun Fact", value = member.funFact)
                DetailRow(label = "📸 Instagram", value = member.instagramHandle)
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "\"${member.quote}\"",
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF475569),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 12.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = onClose,
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3B82F6)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Tutup Profil", fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontWeight = FontWeight.SemiBold, color = Color(0xFF64748B), fontSize = 14.sp)
        Text(text = value, fontWeight = FontWeight.Bold, color = Color(0xFF1E293B), fontSize = 14.sp)
    }
}
```
