"use client";

import { useState } from "react";
import { useRouter } from "next/navigation";

export default function PMINonProsedurPage() {
  const router = useRouter();

  // ── State Identitas Diri ──
  const [nik, setNik] = useState("");
  const [namaLengkap, setNamaLengkap] = useState("");
  const [jenisKelamin, setJenisKelamin] = useState("laki-laki");
  const [tempatLahir, setTempatLahir] = useState("");
  const [tanggalLahir, setTanggalLahir] = useState("");
  const [alamat, setAlamat] = useState("");

  // ── State Penempatan & Kepulangan ──
  const [negara, setNegara] = useState("");
  const [tanggalPulang, setTanggalPulang] = useState("");

  // ── State Detail Kasus ──
  const [permasalahan, setPermasalahan] = useState("");
  const [keterangan, setKeterangan] = useState("");

  const handleSimpan = (e: React.FormEvent) => {
    e.preventDefault();
    // TODO: kirim data ke Spring Boot
    console.log("Data PMI Non Prosedur tersimpan");
  };

  const handleBatal = () => {
    router.back();
  };

  // ── Style reusable ──
  const inputClass =
    "w-full border border-gray-200 rounded-xl px-4 py-3 text-sm text-gray-700 placeholder:text-gray-400 focus:outline-none focus:ring-2 focus:ring-[#1B4EF5]/30 focus:border-[#1B4EF5] transition-all bg-white";
  const selectClass =
    "w-full border border-gray-200 rounded-xl px-4 py-3 text-sm text-gray-500 focus:outline-none focus:ring-2 focus:ring-[#1B4EF5]/30 focus:border-[#1B4EF5] transition-all bg-white appearance-none cursor-pointer";
  const labelClass = "block text-xs font-semibold text-gray-600 mb-1.5";
  const radioClass =
    "flex items-center gap-2 px-4 py-2.5 rounded-xl border-2 cursor-pointer transition-all text-sm font-medium";

  // ── Icon Pesawat ──
  const PlaneIcon = () => (
    <svg className="w-5 h-5 text-[#1B4EF5]" fill="currentColor" viewBox="0 0 24 24">
      <path d="M21 16v-2l-8-5V3.5c0-.83-.67-1.5-1.5-1.5S10 2.67 10 3.5V9l-8 5v2l8-2.5V19l-2 1.5V22l3.5-1l3.5 1v-1.5L13 19v-5.5l8 2.5z" />
    </svg>
  );

  return (
    <div className="space-y-6 pb-10">

      {/* ── Header ── */}
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-2xl font-black text-gray-900">Data PMI</h1>
          <p className="text-sm text-gray-500">Non Prosedural</p>
        </div>
        <button
          onClick={() => router.push("/pmi-non-prosedur/data")}
          className="flex items-center gap-2 border-2 border-[#1B4EF5] text-[#1B4EF5] font-bold text-sm px-5 py-2.5 rounded-full hover:bg-[#1B4EF5] hover:text-white transition-all"
        >
          Lihat data
        </button>
      </div>

      <form onSubmit={handleSimpan} className="space-y-6">

        {/* ══════════════════════════════
            SECTION 1 — Identitas Diri
        ══════════════════════════════ */}
        <div className="bg-white rounded-2xl p-6 border border-gray-100 shadow-sm space-y-5">
          <h2 className="text-lg font-black text-gray-900 flex items-center gap-2">
            <svg className="w-5 h-5 text-[#1B4EF5]" fill="currentColor" viewBox="0 0 24 24">
              <path d="M12 12c2.7 0 4.8-2.1 4.8-4.8S14.7 2.4 12 2.4 7.2 4.5 7.2 7.2 9.3 12 12 12zm0 2.4c-3.2 0-9.6 1.6-9.6 4.8v2.4h19.2v-2.4c0-3.2-6.4-4.8-9.6-4.8z" />
            </svg>
            Identitas diri
          </h2>

          {/* NIK & Nama */}
          <div className="grid grid-cols-2 gap-4">
            <div>
              <label className={labelClass}>NIK (Nomor Induk Kependudukan)</label>
              <input
                type="text"
                maxLength={16}
                placeholder="Masukkan 16 digit NIK"
                value={nik}
                onChange={(e) => setNik(e.target.value.replace(/\D/g, ""))}
                className={inputClass}
              />
            </div>
            <div>
              <label className={labelClass}>Nama Lengkap</label>
              <input
                type="text"
                placeholder="Masukkan nama lengkap"
                value={namaLengkap}
                onChange={(e) => setNamaLengkap(e.target.value)}
                className={inputClass}
              />
            </div>
          </div>

          {/* Jenis Kelamin */}
          <div>
            <label className={labelClass}>Jenis Kelamin</label>
            <div className="flex gap-3">
              {["laki-laki", "perempuan"].map((jk) => (
                <label
                  key={jk}
                  className={`${radioClass} ${
                    jenisKelamin === jk
                      ? "border-[#1B4EF5] bg-[#DDE5FE] text-[#1B4EF5]"
                      : "border-gray-200 text-gray-500 hover:border-[#1B4EF5]/40"
                  }`}
                >
                  <input
                    type="radio"
                    name="jenisKelamin"
                    value={jk}
                    checked={jenisKelamin === jk}
                    onChange={() => setJenisKelamin(jk)}
                    className="accent-[#1B4EF5]"
                  />
                  {jk === "laki-laki" ? "Laki-laki" : "Perempuan"}
                </label>
              ))}
            </div>
          </div>

          {/* Tempat & Tanggal Lahir */}
          <div className="grid grid-cols-2 gap-4">
            <div>
              <label className={labelClass}>Tempat Lahir</label>
              <input
                type="text"
                placeholder="Kab/Kota"
                value={tempatLahir}
                onChange={(e) => setTempatLahir(e.target.value)}
                className={inputClass}
              />
            </div>
            <div>
              <label className={labelClass}>Tanggal Lahir</label>
              <input
                type="date"
                value={tanggalLahir}
                onChange={(e) => setTanggalLahir(e.target.value)}
                className={inputClass}
              />
            </div>
          </div>

          {/* Alamat */}
          <div>
            <label className={labelClass}>Alamat</label>
            <textarea
              placeholder="Alamat lengkap"
              value={alamat}
              onChange={(e) => setAlamat(e.target.value)}
              rows={3}
              className={`${inputClass} resize-none`}
            />
          </div>
        </div>

        {/* ══════════════════════════════
            SECTION 2 — Penempatan & Kepulangan
        ══════════════════════════════ */}
        <div className="bg-white rounded-2xl p-6 border border-gray-100 shadow-sm space-y-5">
          <h2 className="text-lg font-black text-gray-900 flex items-center gap-2">
            <PlaneIcon />
            Penempatan dan kepulangan
          </h2>

          <div className="grid grid-cols-2 gap-4">
            {/* Negara */}
            <div>
              <label className={labelClass}>Negara</label>
              <input
                type="text"
                placeholder="Negara tujuan"
                value={negara}
                onChange={(e) => setNegara(e.target.value)}
                className={inputClass}
              />
            </div>

            {/* Tanggal Pulang */}
            <div>
              <label className={labelClass}>Tanggal pulang</label>
              <input
                type="date"
                value={tanggalPulang}
                onChange={(e) => setTanggalPulang(e.target.value)}
                className={inputClass}
              />
            </div>
          </div>
        </div>

        {/* ══════════════════════════════
            SECTION 3 — Detail Kasus
        ══════════════════════════════ */}
        <div className="bg-white rounded-2xl p-6 border border-gray-100 shadow-sm space-y-5">
          <h2 className="text-lg font-black text-gray-900 flex items-center gap-2">
            <svg className="w-5 h-5 text-[#1B4EF5]" fill="currentColor" viewBox="0 0 24 24">
                <path d="M1 21h22L12 2 1 21zm12-3h-2v-2h2v2zm0-4h-2v-4h2v4z" />
            </svg>
            Detail kasus
          </h2>

          {/* Permasalahan */}
          <div>
            <label className={labelClass}>Permasalahan</label>
            <div className="relative">
              <select
                value={permasalahan}
                onChange={(e) => setPermasalahan(e.target.value)}
                className={selectClass}
              >
                <option value="">Pilih kategori permasalahan</option>
                <option value="deportasi">Deportasi</option>
                <option value="deportasi-sakit">Deportasi sakit</option>
                <option value="meninggal">Meninggal</option>
              </select>
              <svg
                className="w-4 h-4 text-gray-400 absolute right-3 top-1/2 -translate-y-1/2 pointer-events-none"
                fill="none"
                stroke="currentColor"
                viewBox="0 0 24 24"
              >
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 9l-7 7-7-7" />
              </svg>
            </div>
          </div>

          {/* Keterangan / Kronologi */}
          <div>
            <label className={labelClass}>Keterangan / kronologi</label>
            <textarea
              placeholder="Jelaskan secara ringkas kondisi saat dipulangkan"
              value={keterangan}
              onChange={(e) => setKeterangan(e.target.value)}
              rows={5}
              className={`${inputClass} resize-none`}
            />
          </div>
        </div>

        {/* ── Tombol Aksi ── */}
        <div className="flex items-center justify-center gap-4 pt-2">
          <button
            type="submit"
            className="bg-[#1B4EF5] hover:bg-[#1540cc] text-white font-bold px-10 py-3 rounded-full transition-all"
          >
            Simpan data
          </button>
          <button
            type="button"
            onClick={handleBatal}
            className="border-2 border-[#1B4EF5] text-[#1B4EF5] font-bold px-10 py-3 rounded-full hover:bg-[#DDE5FE] transition-all"
          >
            Batal
          </button>
        </div>

      </form>
    </div>
  );
}