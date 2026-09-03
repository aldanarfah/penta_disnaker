"use client";
import Link from "next/link";

const modulUtama = [
  {
    label: "CPMI",
    desc: "Kelola data Calon Pekerja Migran Indonesia",
    href: "/cpmi",
    icon: (
      <svg className="w-10 h-10 text-[#1B4EF5]" fill="currentColor" viewBox="0 0 24 24">
        <path d="M0 0h24v24H0z" fill="none" />
	      <path fill="currentColor" d="M12 3c2.21 0 4 1.79 4 4s-1.79 4-4 4s-4-1.79-4-4s1.79-4 4-4m4 10.54c0 1.06-.28 3.53-2.19 6.29L13 15l.94-1.88c-.62-.07-1.27-.12-1.94-.12s-1.32.05-1.94.12L11 15l-.81 4.83C8.28 17.07 8 14.6 8 13.54c-2.39.7-4 1.96-4 3.46v4h16v-4c0-1.5-1.6-2.76-4-3.46" />
      </svg>
    ),
  },
  {
    label: "AK-1",
    desc: "Kartu kuning tanda pengenal resmi untuk data pencari kerja",
    href: "/ak1",
    icon: (
      <svg className="w-10 h-10 text-[#1B4EF5]" fill="none" stroke="currentColor" viewBox="0 0 24 24">
	      <path fill="currentColor" d="M20 4H4c-1.1 0-2 .9-2 2v12c0 1.1.9 2 2 2h16c1.1 0 2-.9 2-2V6c0-1.1-.9-2-2-2M9 8c1.15 0 2 .85 2 2s-.85 2-2 2s-2-.85-2-2s.85-2 2-2m-3 8c0-1.66 1.34-3 3-3s3 1.34 3 3zm12-1h-4v-2h4zm0-4h-5V9h5z" />
      </svg>
    ),
  },
  {
    label: "PMI Non Prosedural",
    desc: "Pekerja Migran Indonesia yang bekerja di luar negeri secara tidak resmi",
    href: "/pmi-non-prosedur",
    icon: (
      <svg className="w-10 h-10 text-[#1B4EF5]" fill="currentColor" viewBox="0 0 24 24">
        <path fill="currentColor" d="M10 4a4 4 0 0 1 4 4a4 4 0 0 1-4 4a4 4 0 0 1-4-4a4 4 0 0 1 4-4m0 10c4.42 0 8 1.79 8 4v2H2v-2c0-2.21 3.58-4 8-4m10-2V7h2v6h-2m0 4v-2h2v2z" />
      </svg>
    ),
  },
  {
    label: "Disabilitas",
    desc: "Kelola data pekerja disabilitas perusahaan",
    href: "/disabilitas",
    icon: (
      <svg className="w-10 h-10 text-[#1B4EF5]" fill="none" stroke="currentColor" viewBox="0 0 24 24">
        <path fill="currentColor" d="M8 6c0-2.21 1.79-4 4-4s4 1.79 4 4s-1.79 4-4 4s-4-1.79-4-4m9 16h1c1.1 0 2-.9 2-2v-4.78c0-1.12-.61-2.15-1.61-2.66c-.43-.22-.89-.43-1.39-.62zm-4.66-5L15 11.33c-.93-.21-1.93-.33-3-.33c-2.53 0-4.71.7-6.39 1.56A2.97 2.97 0 0 0 4 15.22V22h2.34c-.22-.45-.34-.96-.34-1.5C6 18.57 7.57 17 9.5 17zM10 22l1.41-3H9.5c-.83 0-1.5.67-1.5 1.5S8.67 22 9.5 22z" />
      </svg>
    ),
  },
];

export default function DashboardPage() {
  return (
    <div className="space-y-6">

      {/* Header */}
      <div className="flex items-start justify-between">
        <div>
          <h1 className="text-2xl font-black text-gray-900">
            Selamat Datang, di Dashboard Penta
          </h1>
          <p className="text-gray-500 text-sm mt-1">
            Sistem internal kelola dan pantau data tenaga kerja.
          </p>
        </div>
      </div>

      {/* Grid konten */}
      <div className="grid grid-cols-3 gap-6 items-start">

        {/* Modul Utama — 2/3 lebar */}
        <div className="col-span-2 space-y-4">
          <h2 className="font-bold text-gray-800 flex items-center gap-2">
            <svg className="w-5 h-5 text-[#1B4EF5]" fill="currentColor" viewBox="0 0 24 24">
              <rect x="3" y="3" width="7" height="7" rx="1"/>
              <rect x="14" y="3" width="7" height="7" rx="1"/>
              <rect x="3" y="14" width="7" height="7" rx="1"/>
              <rect x="14" y="14" width="7" height="7" rx="1"/>
            </svg>
            Modul utama
          </h2>

          <div className="grid grid-cols-2 gap-4">
            {modulUtama.map((modul) => (
              <div
                key={modul.href}
                className="bg-white rounded-2xl p-5 border border-gray-100 shadow-sm flex flex-col gap-3 hover:shadow-md transition-shadow"
              > 
                {modul.icon}
                <div>
                  <h3 className="font-bold text-gray-900 text-base">{modul.label}</h3>
                  <p className="text-gray-500 text-sm mt-1 leading-relaxed">{modul.desc}</p>
                </div>
                <Link
                href={
                  modul.label === "CPMI" ? "/cpmi/rekap" : 
                  modul.label === "PMI Non Prosedural" ? "/pmi-non-prosedur/rekap" :
                  modul.label === "Disabilitas" ? "/disabilitas/rekap" :
                  modul.label === "AK-1" ? "/ak1/rekap" :
                  modul.href}
                className="text-[#1B4EF5] text-sm font-medium hover:underline mt-auto"
              >
                Lihat selengkapnya
              </Link>
              </div>
            ))}
          </div>
        </div>

        {/* Manajemen Pengguna — 1/3 lebar */}
        <div className="space-y-4">
          <h2 className="font-bold text-gray-800 flex items-center gap-2">
            <svg className="w-5 h-5 text-[#1B4EF5]" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2}
                d="M10.325 4.317c.426-1.756 2.924-1.756 3.35 0a1.724 1.724 0 002.573 1.066c1.543-.94 3.31.826 2.37 2.37a1.724 1.724 0 001.065 2.572c1.756.426 1.756 2.924 0 3.35a1.724 1.724 0 00-1.066 2.573c.94 1.543-.826 3.31-2.37 2.37a1.724 1.724 0 00-2.572 1.065c-.426 1.756-2.924 1.756-3.35 0a1.724 1.724 0 00-2.573-1.066c-1.543.94-3.31-.826-2.37-2.37a1.724 1.724 0 00-1.065-2.572c-1.756-.426-1.756-2.924 0-3.35a1.724 1.724 0 001.066-2.573c-.94-1.543.826-3.31 2.37-2.37.996.608 2.296.07 2.572-1.065z"/>
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M15 12a3 3 0 11-6 0 3 3 0 016 0z"/>
            </svg>
            Manajemen pengguna
          </h2>

          <div className="bg-white rounded-2xl p-5 border border-gray-100 shadow-sm space-y-4">
            <p className="text-sm text-gray-500">(Khusus Admin)</p>

            {/* Total Admin */}
            <div className="flex items-center gap-3 bg-[#DDE5FE] rounded-xl px-4 py-3">
              <svg className="w-5 h-5 text-[#1B4EF5]" fill="currentColor" viewBox="0 0 24 24">
                <path d="M12 12c2.7 0 4.8-2.1 4.8-4.8S14.7 2.4 12 2.4 7.2 4.5 7.2 7.2 9.3 12 12 12zm0 2.4c-3.2 0-9.6 1.6-9.6 4.8v2.4h19.2v-2.4c0-3.2-6.4-4.8-9.6-4.8z"/>
              </svg>
              <span className="text-sm text-gray-700 flex-1">Total Admin</span>
              <span className="font-bold text-[#1B4EF5] text-lg">1</span>
            </div>

            {/* Total Pegawai */}
            <div className="flex items-center gap-3 bg-[#DDE5FE] rounded-xl px-4 py-3">
              <svg className="w-5 h-5 text-[#1B4EF5]" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2}
                  d="M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0z"/>
              </svg>
              <span className="text-sm text-gray-700 flex-1">Total Pegawai</span>
              <span className="font-bold text-[#1B4EF5] text-lg">4</span>
            </div>

            {/* Avatar placeholder */}
            <div className="flex justify-center py-4">
              <div className="w-20 h-20 rounded-full bg-[#DDE5FE] border-2 border-[#1B4EF5]/20 flex items-center justify-center">
                <svg className="w-10 h-10 text-[#1B4EF5]/40" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={1.5}
                    d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z"/>
                </svg>
              </div>
            </div>

            {/* Tombol Kelola Akses */}
            <button
              onClick={() => window.location.href = "/manajemen-pengguna"}
              className="w-full bg-[#1B4EF5] hover:bg-[#1540cc] text-white text-sm font-bold rounded-xl py-3 transition-colors">
              Kelola akses
            </button>
          </div>
        </div>

      </div>
    </div>
  );
}