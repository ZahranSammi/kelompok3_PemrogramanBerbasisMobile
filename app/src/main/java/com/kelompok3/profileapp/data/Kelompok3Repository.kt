package com.kelompok3.profileapp.data

import com.kelompok3.profileapp.R
import com.kelompok3.profileapp.model.MemberProfile

object Kelompok3Repository {

    // Data riil seluruh anggota Kelompok 3 dengan jabatan Ketua Kelompok dan Anggota (R-38).
    fun getMembers(): List<MemberProfile> = listOf(
        MemberProfile(
            id = "member-1",
            fullName = "Parisha Alea Rizkianu Tigusti",
            nickName = "Alea",
            nim = "2024240002",
            role = "Ketua Kelompok",
            roleCategory = "Ketua",
            isLeader = true,
            major = "Sistem Informasi",
            phoneNumber = "089601647279",
            email = "parishaalea29@gmail.com",
            accentColorHex = 0xFFFFF3C4,
            borderColorHex = 0xFFD97706,
            avatarRes = R.drawable.lea
        ),
        MemberProfile(
            id = "member-2",
            fullName = "Wibi Dwi Andriyanto",
            nickName = "Wibi",
            nim = "2024240009",
            role = "Anggota",
            roleCategory = "Anggota",
            isLeader = false,
            major = "Sistem Informasi",
            phoneNumber = "085692527303",
            email = "wibidwiandriyanto.12@gmail.com",
            accentColorHex = 0xFFE0F2FE,
            borderColorHex = 0xFF2563EB,
            avatarRes = R.drawable.wibi
        ),
        MemberProfile(
            id = "member-3",
            fullName = "Dewi Anggreayani",
            nickName = "Dewi",
            nim = "2024240022",
            role = "Anggota",
            roleCategory = "Anggota",
            isLeader = false,
            major = "Sistem Informasi",
            phoneNumber = "085772314598",
            email = "dewi182004@gmail.com",
            accentColorHex = 0xFFFFFFFF,
            borderColorHex = 0xFF2563EB,
            avatarRes = R.drawable.dewi
        ),
        MemberProfile(
            id = "member-4",
            fullName = "Faiza Fitri Farikhatusshifa",
            nickName = "Faiza",
            nim = "2024240016",
            role = "Anggota",
            roleCategory = "Anggota",
            isLeader = false,
            major = "Sistem Informasi",
            phoneNumber = "087875214383",
            email = "agathafayy@gmail.com",
            accentColorHex = 0xFFFFFFFF,
            borderColorHex = 0xFF2563EB,
            avatarRes = R.drawable.faiza
        ),
        MemberProfile(
            id = "member-5",
            fullName = "Zahran Sammi",
            nickName = "Zahran",
            nim = "2024240018",
            role = "Anggota",
            roleCategory = "Anggota",
            isLeader = false,
            major = "Sistem Informasi",
            phoneNumber = "082133317802",
            email = "zahransammi2005@gmail.com",
            accentColorHex = 0xFFE0F2FE,
            borderColorHex = 0xFF2563EB,
            avatarRes = R.drawable.zahran
        )
    )

    val roleCategories: List<String> = listOf(
        "Semua",
        "Ketua",
        "Anggota"
    )
}
