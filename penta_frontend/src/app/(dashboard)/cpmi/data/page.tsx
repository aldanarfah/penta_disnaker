"use client";

import { useState } from "react";
import { useRouter } from "next/navigation";

type DataCPMI = {
  id: number;
  namaLengkap: string;
  nik: string;
  jenisKelamin: string;
  tempatLahir: string;
  tanggalLahir: string;
  alamat: string;
  kecamatan: string;
  desa: string;
  pendidikan: string;
  noTelepon: string;
  tanggalRekom: string;
  keterangan: string;
  jabatan: string;
  negaraTujuan: string;
  perusahaanPengirim: string;
  pemberiKerja: string;
  bidang: string;
  kompetensi: string;
  noReg: string;
  noSertifikat: string;
};

const dummyData: DataCPMI[] = [
  {
    id: 1,
    namaLengkap: "Siti Aminah",
    nik: "3508012345678901",
    jenisKelamin: "Perempuan",
    tempatLahir: "Lumajang",
    tanggalLahir: "01/05/1995",
    alamat: "Desa Tempeh Lor No.5 Kecamatan Tempeh, Lumajang",
    kecamatan: "Tempeh",
    desa: "Tempeh Lor",
    pendidikan: "SMA",
    noTelepon: "081234567890",
    tanggalRekom: "10/01/2025",
    keterangan: "RK-2025-001",
    jabatan: "Caregiver",
    negaraTujuan: "Hongkong",
    perusahaanPengirim: "PT Maju Bersama",
    pemberiKerja: "Mr. Chan",
    bidang: "Domestic Worker",
    kompetensi: "Housekeeper",
    noReg: "REG-2025-001",
    noSertifikat: "SERT-2025-001",
  },
];

export default function DataCPMIPage() {
  const router = useRouter();
  const [search, setSearch] = useState("");
  const [data, setData] = useState<DataCPMI[]>(dummyData);
  const [currentPage, setCurrentPage] = useState(1);
  const [showDeleteModal, setShowDeleteModal] = useState(false);
  const [deleteId, setDeleteId] = useState<number | null>(null);
  const itemsPerPage = 10;

  const filtered = data.filter(
    (d) =>
      d.namaLengkap.toLowerCase().includes(search.toLowerCase()) ||
      d.nik.includes(search)
  );

  const totalPages = Math.max(1, Math.ceil(filtered.length / itemsPerPage));
  const paginated = filtered.slice(
    (currentPage - 1) * itemsPerPage,
    currentPage * itemsPerPage
  );

  const handleDeleteConfirm = () => {
    if (deleteId !== null) {
      setData((prev) => prev.filter((d) => d.id !== deleteId));
      setShowDeleteModal(false);
      setDeleteId(null);
    }
  };

  const renderPageNumbers = () => {
    const pages: (number | string)[] = [];
    if (totalPages <= 5) {
      for (let i = 1; i <= totalPages; i++) pages.push(i);
    } else {
      pages.push(1, 2, 3, "...", totalPages);
    }
    return pages;
  };

  const columns = [
    "Nama Lengkap", "NIK", "Jenis Kelamin", "Tempat Lahir", "Tanggal Lahir",
    "Alamat", "Kecamatan", "Desa", "Pendidikan", "No Telepon",
    "Tanggal Rekom", "Keterangan", "Jabatan", "Negara Tujuan",
    "Perusahaan Pengirim", "Pemberi Kerja", "Bidang", "Kompetensi",
    "No Reg", "No Sertifikat", "Aksi",
  ];

  return (
    <div className="space-y-5 pb-10">

      {/* ── Header ── */}
      <div className="flex items-end justify-between gap-4 flex-wrap">
        <div>
          <h1 className="text-2xl font-black text-gray-900">Daftar Rekom ID CPMI</h1>
          <p className="text-sm text-gray-500">Calon Pekerja Migran Indonesia</p>
        </div>

        <div className="flex items-center gap-3 flex-wrap">
          {/* Search */}
          <div className="flex items-center gap-2 bg-white border-2 border-gray-200 rounded-full px-4 py-2 w-72 focus-within:border-[#1B4EF5] transition-all">
            <svg className="w-4 h-4 text-gray-400 shrink-0" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2}
                d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
            </svg>
            <input
              type="text"
              placeholder="Cari NIK atau nama..."
              value={search}
              onChange={(e) => { setSearch(e.target.value); setCurrentPage(1); }}
              className="flex-1 bg-transparent outline-none text-sm text-gray-700 placeholder:text-gray-400"
            />
          </div>

          {/* Ekspor Excel */}
          <button
            onClick={() => alert("Ekspor Excel belum terhubung ke backend.")}
            className="flex items-center gap-2 bg-white border border-gray-200 hover:border-green-500 text-gray-700 hover:text-green-600 text-sm font-medium px-4 py-2 rounded-xl transition-all shadow-sm"
          >
            <svg className="w-4 h-4 text-green-600" fill="currentColor" viewBox="0 0 24 24">
              <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8zm-1 7H8v2h5v-2zm0 4H8v2h5v-2zM8 9h2V7H8v2zm6-5v3h3l-3-3z" />
            </svg>
            Ekspor Excel
          </button>

          {/* Ekspor PDF */}
          <button
            onClick={() => alert("Ekspor PDF belum terhubung ke backend.")}
            className="flex items-center gap-2 bg-white border border-gray-200 hover:border-red-400 text-gray-700 hover:text-red-500 text-sm font-medium px-4 py-2 rounded-xl transition-all shadow-sm"
          >
            <svg className="w-4 h-4 text-red-500" fill="currentColor" viewBox="0 0 24 24">
              <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8zm-1 9H8v-2h5v2zm2-4H8V5h7v2z" />
            </svg>
            Ekspor PDF
          </button>
        </div>
      </div>

      {/* ── Tabel ── */}
      <div className="bg-white rounded-2xl border border-gray-100 shadow-sm overflow-hidden">
        <div className="overflow-x-auto">
          <table className="w-full text-sm min-w-[2400px]">
            <thead>
              <tr className="bg-[#DDE5FE] text-gray-700">
                {columns.map((col) => (
                  <th key={col} className={`text-left px-4 py-3 font-semibold whitespace-nowrap ${col === "Aksi" ? "text-center" : ""}`}>
                    {col}
                  </th>
                ))}
              </tr>
            </thead>
            <tbody className="divide-y divide-gray-100">
              {paginated.length === 0 ? (
                <tr>
                  <td colSpan={columns.length} className="text-center py-16 text-gray-400 text-sm">
                    Tidak ada data ditemukan
                  </td>
                </tr>
              ) : (
                paginated.map((item) => (
                  <tr key={item.id} className="hover:bg-gray-50 transition-colors">
                    <td className="px-4 py-3 whitespace-nowrap font-medium text-gray-800">{item.namaLengkap}</td>
                    <td className="px-4 py-3 whitespace-nowrap text-gray-600">{item.nik}</td>
                    <td className="px-4 py-3 whitespace-nowrap text-gray-600">{item.jenisKelamin}</td>
                    <td className="px-4 py-3 whitespace-nowrap text-gray-600">{item.tempatLahir}</td>
                    <td className="px-4 py-3 whitespace-nowrap text-gray-600">{item.tanggalLahir}</td>
                    <td className="px-4 py-3 text-gray-600 max-w-[200px]">{item.alamat}</td>
                    <td className="px-4 py-3 whitespace-nowrap text-gray-600">{item.kecamatan}</td>
                    <td className="px-4 py-3 whitespace-nowrap text-gray-600">{item.desa}</td>
                    <td className="px-4 py-3 whitespace-nowrap text-gray-600">{item.pendidikan}</td>
                    <td className="px-4 py-3 whitespace-nowrap text-gray-600">{item.noTelepon}</td>
                    <td className="px-4 py-3 whitespace-nowrap text-gray-600">{item.tanggalRekom}</td>
                    <td className="px-4 py-3 whitespace-nowrap text-gray-600">{item.keterangan}</td>
                    <td className="px-4 py-3 whitespace-nowrap text-gray-600">{item.jabatan}</td>
                    <td className="px-4 py-3 whitespace-nowrap text-gray-600">{item.negaraTujuan}</td>
                    <td className="px-4 py-3 whitespace-nowrap text-gray-600">{item.perusahaanPengirim}</td>
                    <td className="px-4 py-3 whitespace-nowrap text-gray-600">{item.pemberiKerja}</td>
                    <td className="px-4 py-3 whitespace-nowrap text-gray-600">{item.bidang}</td>
                    <td className="px-4 py-3 whitespace-nowrap text-gray-600">{item.kompetensi}</td>
                    <td className="px-4 py-3 whitespace-nowrap text-gray-600">{item.noReg}</td>
                    <td className="px-4 py-3 whitespace-nowrap text-gray-600">{item.noSertifikat}</td>

                    {/* Aksi */}
                    <td className="px-4 py-3 whitespace-nowrap">
                      <div className="flex items-center justify-center gap-2">
                        <button
                          onClick={() => router.push(`/cpmi/edit/${item.id}`)}
                          className="flex items-center gap-1.5 bg-[#DDE5FE] hover:bg-[#1B4EF5] text-[#1B4EF5] hover:text-white text-xs font-semibold px-3 py-1.5 rounded-lg transition-all"
                        >
                          <svg className="w-3.5 h-3.5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2}
                              d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z" />
                          </svg>
                          Edit
                        </button>
                        <button
                          onClick={() => { setDeleteId(item.id); setShowDeleteModal(true); }}
                          className="flex items-center gap-1.5 bg-red-50 hover:bg-red-500 text-red-500 hover:text-white text-xs font-semibold px-3 py-1.5 rounded-lg transition-all"
                        >
                          <svg className="w-3.5 h-3.5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2}
                              d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" />
                          </svg>
                          Hapus
                        </button>
                      </div>
                    </td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        </div>

        {/* ── Pagination ── */}
        <div className="flex items-center justify-end gap-1 px-4 py-4 border-t border-gray-100">
          <button
            onClick={() => setCurrentPage((p) => Math.max(1, p - 1))}
            disabled={currentPage === 1}
            className="w-8 h-8 flex items-center justify-center rounded-lg border border-gray-200 text-gray-500 hover:border-[#1B4EF5] hover:text-[#1B4EF5] disabled:opacity-40 disabled:cursor-not-allowed transition-all"
          >
            <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M15 19l-7-7 7-7" />
            </svg>
          </button>

          {renderPageNumbers().map((page, idx) =>
            page === "..." ? (
              <span key={idx} className="w-8 h-8 flex items-center justify-center text-gray-400 text-sm">...</span>
            ) : (
              <button
                key={idx}
                onClick={() => setCurrentPage(Number(page))}
                className={`w-8 h-8 flex items-center justify-center rounded-lg text-sm font-medium transition-all ${
                  currentPage === page
                    ? "bg-[#1B4EF5] text-white shadow"
                    : "border border-gray-200 text-gray-600 hover:border-[#1B4EF5] hover:text-[#1B4EF5]"
                }`}
              >
                {page}
              </button>
            )
          )}

          <button
            onClick={() => setCurrentPage((p) => Math.min(totalPages, p + 1))}
            disabled={currentPage === totalPages}
            className="w-8 h-8 flex items-center justify-center rounded-lg border border-gray-200 text-gray-500 hover:border-[#1B4EF5] hover:text-[#1B4EF5] disabled:opacity-40 disabled:cursor-not-allowed transition-all"
          >
            <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 5l7 7-7 7" />
            </svg>
          </button>
        </div>
      </div>

      {/* ── Modal Konfirmasi Hapus ── */}
      {showDeleteModal && (
        <div className="fixed inset-0 bg-black/40 flex items-center justify-center z-50">
          <div className="bg-white rounded-2xl p-6 shadow-xl w-full max-w-sm mx-4 space-y-4">
            <div className="flex items-center gap-3">
              <div className="w-10 h-10 rounded-full bg-red-100 flex items-center justify-center shrink-0">
                <svg className="w-5 h-5 text-red-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2}
                    d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" />
                </svg>
              </div>
              <div>
                <h3 className="font-bold text-gray-900">Hapus Data</h3>
                <p className="text-sm text-gray-500">Data yang dihapus tidak bisa dikembalikan.</p>
              </div>
            </div>
            <p className="text-sm text-gray-600">Apakah kamu yakin ingin menghapus data ini?</p>
            <div className="flex gap-3 pt-1">
              <button
                onClick={() => { setShowDeleteModal(false); setDeleteId(null); }}
                className="flex-1 border-2 border-gray-200 text-gray-600 font-semibold py-2.5 rounded-xl hover:border-gray-300 transition-all text-sm"
              >
                Batal
              </button>
              <button
                onClick={handleDeleteConfirm}
                className="flex-1 bg-red-500 hover:bg-red-600 text-white font-semibold py-2.5 rounded-xl transition-all text-sm"
              >
                Ya, Hapus
              </button>
            </div>
          </div>
        </div>
      )}

    </div>
  );
}