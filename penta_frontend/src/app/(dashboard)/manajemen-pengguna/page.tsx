"use client";

import { useState } from "react";

// ── Tipe ──
type RoleType = "Super Admin" | "Verifikator Desa" | "Operator Dinas" | "Staf Lapangan";
type Pegawai = {
  id: number;
  nama: string;
  nip: string;
  role: RoleType;
  aktif: boolean;
  username: string;
  password: string;
  inisial: string;
  warna: string;
};

const ROLE_LIST: RoleType[] = ["Super Admin", "Verifikator Desa", "Operator Dinas", "Staf Lapangan"];

const ROLE_WARNA: Record<RoleType, string> = {
  "Super Admin":      "bg-purple-100 text-purple-700",
  "Verifikator Desa": "bg-orange-100 text-orange-700",
  "Operator Dinas":   "bg-blue-100 text-[#1B4EF5]",
  "Staf Lapangan":    "bg-gray-100 text-gray-600",
};

const WARNA_INISIAL = [
  "bg-[#1B4EF5]", "bg-teal-500", "bg-purple-500",
  "bg-orange-500", "bg-pink-500", "bg-green-600",
];

function getInisial(nama: string) {
  return nama.split(" ").map((n) => n[0]).join("").toUpperCase().slice(0, 2);
}

// ── Data dummy ──
const dataDummy: Pegawai[] = [
  { id:1,  nama:"Budi Waseso",      nip:"198005122005011004", role:"Operator Dinas", aktif:true,  username:"budi.waseso",    password:"budi1234",    inisial:"BW", warna:"bg-[#1B4EF5]" },
  { id:2,  nama:"Siti Maimunah",    nip:"198511202010012008", role:"Operator Dinas",   aktif:true,  username:"siti.maimunah",  password:"siti1234",    inisial:"SM", warna:"bg-teal-500"   },
  { id:3,  nama:"Agus Susanto",     nip:"PIIT-2023-045",      role:"Staf Lapangan",    aktif:false, username:"agus.susanto",   password:"agus1234",    inisial:"AS", warna:"bg-gray-400"   },
];

// ── Komponen Indikator kekuatan password ──
function KekuatanPassword({ password }: { password: string }) {
  const panjang  = password.length >= 8;
  const hurufBesar = /[A-Z]/.test(password);
  const angka    = /[0-9]/.test(password);
  const simbol   = /[^A-Za-z0-9]/.test(password);
  const skor     = [panjang, hurufBesar, angka, simbol].filter(Boolean).length;
  const label    = ["", "Lemah", "Cukup", "Kuat", "Sangat Kuat"];
  const warna    = ["", "bg-red-400", "bg-orange-400", "bg-yellow-400", "bg-green-500"];
  const teks     = ["", "text-red-500", "text-orange-500", "text-yellow-600", "text-green-600"];
  if (!password) return null;
  return (
    <div className="mt-1.5">
      <div className="flex gap-1 mb-1">
        {[1,2,3,4].map((i) => (
          <div key={i} className={`h-1 flex-1 rounded-full transition-all ${i <= skor ? warna[skor] : "bg-gray-200"}`} />
        ))}
      </div>
      <p className={`text-xs font-semibold ${teks[skor]}`}>Kekuatan: {label[skor]}</p>
    </div>
  );
}

export default function ManajemenPenggunaPage() {
  const [daftarPegawai, setDaftarPegawai] = useState<Pegawai[]>(dataDummy);
  const [search, setSearch] = useState("");
  const [currentPage, setCurrentPage] = useState(1);
  const itemsPerPage = 3;

  // ── State Admin ──
  const [adminPasswordSaatIni, setAdminPasswordSaatIni] = useState("");
  const [adminPasswordBaru, setAdminPasswordBaru]       = useState("");
  const [showAdminPass, setShowAdminPass]               = useState(false);
  const [showAdminNew, setShowAdminNew]                 = useState(false);
  const [adminSimpanMsg, setAdminSimpanMsg]             = useState("");

  // ── State Modal Tambah Pegawai ──
  const [showModal, setShowModal]       = useState(false);
  const [modalMode, setModalMode]       = useState<"tambah" | "edit">("tambah");
  const [editId, setEditId]             = useState<number | null>(null);
  const [formNama, setFormNama]         = useState("");
  const [formNip, setFormNip]           = useState("");
  const [formRole, setFormRole]         = useState<RoleType>("Staf Lapangan");
  const [formUsername, setFormUsername] = useState("");
  const [formPassword, setFormPassword] = useState("");
  const [showFormPass, setShowFormPass] = useState(false);
  const [formError, setFormError]       = useState("");

  // ── State Modal Delete ──
  const [showDelete, setShowDelete]     = useState(false);
  const [deleteId, setDeleteId]         = useState<number | null>(null);

  // ── State Edit Password Pegawai ──
  const [showEditPass, setShowEditPass]     = useState(false);
  const [editPassId, setEditPassId]         = useState<number | null>(null);
  const [editPassNama, setEditPassNama]     = useState("");
  const [editPassBaru, setEditPassBaru]     = useState("");
  const [showEditPassVal, setShowEditPassVal] = useState(false);

  const totalPegawai  = daftarPegawai.length;
  const totalAktif = daftarPegawai.filter((p) => p.aktif).length;
  const totalNonAktif = totalPegawai - totalAktif;

  // ── Filter & Pagination ──
  const filtered = daftarPegawai.filter((p) =>
    p.nama.toLowerCase().includes(search.toLowerCase()) ||
    p.nip.toLowerCase().includes(search.toLowerCase())
  );
  const totalPages = Math.max(1, Math.ceil(filtered.length / itemsPerPage));
  const paginated  = filtered.slice((currentPage - 1) * itemsPerPage, currentPage * itemsPerPage);

  // ── Simpan profil admin ──
  const handleSimpanAdmin = () => {
    if (!adminPasswordSaatIni) { setAdminSimpanMsg("❌ Masukkan password saat ini."); return; }
    if (adminPasswordBaru.length < 8) { setAdminSimpanMsg("❌ Password baru minimal 8 karakter."); return; }
    setAdminSimpanMsg("✅ Password berhasil diperbarui!");
    setAdminPasswordSaatIni("");
    setAdminPasswordBaru("");
    setTimeout(() => setAdminSimpanMsg(""), 3000);
  };

  // ── Toggle aktif pegawai ──
  const handleToggleAktif = (id: number) => {
    setDaftarPegawai((prev) => prev.map((p) => p.id === id ? { ...p, aktif: !p.aktif } : p));
  };

  // ── Buka modal tambah ──
  const handleTambah = () => {
    setModalMode("tambah");
    setFormNama(""); setFormNip(""); setFormRole("Staf Lapangan");
    setFormUsername(""); setFormPassword(""); setFormError("");
    setShowModal(true);
  };

  // ── Buka modal edit ──
  const handleEdit = (p: Pegawai) => {
    setModalMode("edit");
    setEditId(p.id);
    setFormNama(p.nama); setFormNip(p.nip); setFormRole(p.role);
    setFormUsername(p.username); setFormPassword(""); setFormError("");
    setShowModal(true);
  };

  // ── Simpan tambah/edit ──
  const handleSimpanModal = () => {
    if (!formNama.trim())     { setFormError("Nama wajib diisi."); return; }
    if (!formNip.trim())      { setFormError("NIP/ID wajib diisi."); return; }
    if (!formUsername.trim()) { setFormError("Username wajib diisi."); return; }
    if (modalMode === "tambah" && formPassword.length < 8) {
      setFormError("Password minimal 8 karakter."); return;
    }
    if (modalMode === "tambah") {
      const idx = daftarPegawai.length % WARNA_INISIAL.length;
      const baru: Pegawai = {
        id: Date.now(), nama: formNama, nip: formNip, role: formRole,
        aktif: true, username: formUsername, password: formPassword,
        inisial: getInisial(formNama), warna: WARNA_INISIAL[idx],
      };
      setDaftarPegawai((prev) => [...prev, baru]);
    } else {
      setDaftarPegawai((prev) => prev.map((p) =>
        p.id === editId ? {
          ...p, nama: formNama, nip: formNip, role: formRole, username: formUsername,
          ...(formPassword.length >= 8 ? { password: formPassword } : {}),
          inisial: getInisial(formNama),
        } : p
      ));
    }
    setShowModal(false);
  };

  // ── Hapus pegawai ──
  const handleHapus = () => {
    if (deleteId !== null) {
      setDaftarPegawai((prev) => prev.filter((p) => p.id !== deleteId));
      setShowDelete(false); setDeleteId(null);
    }
  };

  // ── Simpan password pegawai ──
  const handleSimpanPassPegawai = () => {
    if (editPassBaru.length < 8) return;
    setDaftarPegawai((prev) => prev.map((p) =>
      p.id === editPassId ? { ...p, password: editPassBaru } : p
    ));
    setShowEditPass(false); setEditPassBaru(""); setEditPassId(null);
  };

  const inputClass = "w-full border border-gray-200 rounded-xl px-4 py-2.5 text-sm text-gray-700 placeholder:text-gray-400 focus:outline-none focus:ring-2 focus:ring-[#1B4EF5]/30 focus:border-[#1B4EF5] transition-all bg-white";
  const selectClass = "w-full border border-gray-200 rounded-xl px-4 py-2.5 text-sm text-gray-700 focus:outline-none focus:ring-2 focus:ring-[#1B4EF5]/30 focus:border-[#1B4EF5] transition-all bg-white appearance-none cursor-pointer";
  const labelClass = "block text-xs font-bold text-gray-500 tracking-widest uppercase mb-1.5";

  return (
    <div className="space-y-6 pb-10">

      {/* ── Header ── */}
      <div>
        <p className="text-sm text-gray-500">Sistem PENTA</p>
        <h1 className="text-2xl font-black text-gray-900">Manajemen Pengguna</h1>
      </div>

      <div className="grid grid-cols-5 gap-6 items-start">

        {/* ══════════════════════════════
            KOLOM KIRI — Profil Admin
        ══════════════════════════════ */}
        <div className="col-span-2 bg-white rounded-2xl p-6 border border-gray-100 shadow-sm space-y-5">
          <div>
            <h2 className="font-black text-gray-900 text-lg">Profil Saya</h2>
            <p className="text-xs text-gray-400 mt-0.5">Kelola kredensial keamanan untuk akun administrator Anda.</p>
          </div>

          {/* Avatar admin */}
          <div className="flex items-center gap-4">
            <div className="w-16 h-16 rounded-2xl bg-[#1B4EF5] flex items-center justify-center shadow-md">
              <svg className="w-9 h-9 text-white" fill="currentColor" viewBox="0 0 24 24">
                <path d="M12 1L3 5v6c0 5.55 3.84 10.74 9 12 5.16-1.26 9-6.45 9-12V5l-9-4zm0 10.99h7c-.53 4.12-3.28 7.79-7 8.94V12H5V6.3l7-3.11v8.8z" />
              </svg>
            </div>
            <div>
              <p className="font-black text-gray-900 text-xl">Admin PENTA</p>
              <span className="text-xs bg-[#1B4EF5] text-white px-3 py-1 rounded-full font-semibold">Admin</span>
            </div>
          </div>

          <div className="border-t border-gray-100" />

          {/* Username */}
          <div>
            <label className={labelClass}>Username</label>
            <input type="text" value="admin_penta_lumajang" readOnly className={`${inputClass} bg-gray-50 text-gray-500 cursor-not-allowed`} />
          </div>

          {/* Password saat ini */}
          <div>
            <label className={labelClass}>Password Saat Ini</label>
            <div className="relative">
              <input
                type={showAdminPass ? "text" : "password"}
                placeholder="••••••••"
                value={adminPasswordSaatIni}
                onChange={(e) => setAdminPasswordSaatIni(e.target.value)}
                className={inputClass}
              />
              <button type="button" onClick={() => setShowAdminPass(!showAdminPass)}
                className="absolute right-3 top-1/2 -translate-y-1/2 text-gray-400 hover:text-gray-600">
                {showAdminPass
                  ? <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M13.875 18.825A10.05 10.05 0 0112 19c-4.478 0-8.268-2.943-9.543-7a9.97 9.97 0 011.563-3.029m5.858.908a3 3 0 114.243 4.243M9.878 9.878l4.242 4.242M9.88 9.88l-3.29-3.29m7.532 7.532l3.29 3.29M3 3l3.59 3.59m0 0A9.953 9.953 0 0112 5c4.478 0 8.268 2.943 9.543 7a10.025 10.025 0 01-4.132 4.411m0 0L21 21"/></svg>
                  : <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M15 12a3 3 0 11-6 0 3 3 0 016 0z"/><path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z"/></svg>
                }
              </button>
            </div>
          </div>

          {/* Password baru */}
          <div>
            <label className={labelClass}>Password Baru</label>
            <div className="relative">
              <input
                type={showAdminNew ? "text" : "password"}
                placeholder="Minimal 8 karakter"
                value={adminPasswordBaru}
                onChange={(e) => setAdminPasswordBaru(e.target.value)}
                className={inputClass}
              />
              <button type="button" onClick={() => setShowAdminNew(!showAdminNew)}
                className="absolute right-3 top-1/2 -translate-y-1/2 text-gray-400 hover:text-gray-600">
                {showAdminNew
                  ? <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M13.875 18.825A10.05 10.05 0 0112 19c-4.478 0-8.268-2.943-9.543-7a9.97 9.97 0 011.563-3.029m5.858.908a3 3 0 114.243 4.243M9.878 9.878l4.242 4.242M9.88 9.88l-3.29-3.29m7.532 7.532l3.29 3.29M3 3l3.59 3.59m0 0A9.953 9.953 0 0112 5c4.478 0 8.268 2.943 9.543 7a10.025 10.025 0 01-4.132 4.411m0 0L21 21"/></svg>
                  : <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M15 12a3 3 0 11-6 0 3 3 0 016 0z"/><path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z"/></svg>
                }
              </button>
            </div>
            <KekuatanPassword password={adminPasswordBaru} />
          </div>

          {adminSimpanMsg && (
            <p className={`text-xs font-semibold px-3 py-2 rounded-lg ${adminSimpanMsg.startsWith("✅") ? "bg-green-50 text-green-600" : "bg-red-50 text-red-500"}`}>
              {adminSimpanMsg}
            </p>
          )}

          <button
            onClick={handleSimpanAdmin}
            className="w-full bg-[#1B4EF5] hover:bg-[#1540cc] text-white font-bold py-3 rounded-xl transition-all flex items-center justify-center gap-2"
          >
            <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M8 7H5a2 2 0 00-2 2v9a2 2 0 002 2h14a2 2 0 002-2V9a2 2 0 00-2-2h-3m-1 4l-3 3m0 0l-3-3m3 3V4" />
            </svg>
            Simpan Perubahan
          </button>
        </div>

        {/* ══════════════════════════════
            KOLOM KANAN — Manajemen Akun Pegawai
        ══════════════════════════════ */}
        <div className="col-span-3 space-y-4">

          {/* Header kanan */}
          <div className="flex items-start justify-between">
            <div>
              <h2 className="font-black text-gray-900 text-lg">Manajemen Akun Pegawai</h2>
              <p className="text-xs text-gray-400 mt-0.5">Daftar pegawai dengan akses ke Sistem PENTA Lumajang.</p>
            </div>
            <button
              onClick={handleTambah}
              className="flex items-center gap-2 bg-white border-2 border-[#1B4EF5] text-[#1B4EF5] hover:bg-[#1B4EF5] hover:text-white font-bold text-sm px-4 py-2.5 rounded-xl transition-all"
            >
              <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M18 9v3m0 0v3m0-3h3m-3 0h-3m-2-5a4 4 0 11-8 0 4 4 0 018 0zM3 20a6 6 0 0112 0v1H3v-1z" />
              </svg>
              Tambah Pegawai
            </button>
          </div>

          {/* Summary cards */}
          <div className="grid grid-cols-3 gap-3">
            {[
              { label:"TOTAL PEGAWAI",   value: totalPegawai,    icon:"👥", bg:"bg-[#DDE5FE]",  tc:"text-[#1B4EF5]" },
              { label:"AKTIF",        value: totalAktif,   icon:"✅", bg:"bg-green-50",    tc:"text-green-600" },
              { label:"NON-AKTIF",    value: totalNonAktif,icon:"🚫", bg:"bg-red-50",      tc:"text-red-500"   },
            ].map((c) => (
              <div key={c.label} className="bg-white rounded-2xl p-4 border border-gray-100 shadow-sm flex items-center gap-3">
                <div className={`w-12 h-12 rounded-xl ${c.bg} flex items-center justify-center text-xl shrink-0`}>{c.icon}</div>
                <div>
                  <p className={`text-2xl font-black ${c.tc}`}>{c.value}</p>
                  <p className="text-xs text-gray-500 font-semibold">{c.label}</p>
                </div>
              </div>
            ))}
          </div>

          {/* Search */}
          <div className="flex items-center gap-2 bg-white border border-gray-200 rounded-xl px-4 py-2.5 focus-within:border-[#1B4EF5] transition-all">
            <svg className="w-4 h-4 text-gray-400 shrink-0" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
            </svg>
            <input
              type="text" placeholder="Cari nama atau NIP..."
              value={search} onChange={(e) => { setSearch(e.target.value); setCurrentPage(1); }}
              className="flex-1 bg-transparent outline-none text-sm text-gray-700 placeholder:text-gray-400"
            />
          </div>

          {/* Tabel pegawai */}
          <div className="bg-white rounded-2xl border border-gray-100 shadow-sm overflow-hidden">
            {/* Header tabel */}
            <div className="grid grid-cols-12 gap-2 px-4 py-3 bg-[#DDE5FE] text-xs font-semibold text-gray-600">
              <div className="col-span-4">NAMA PEGAWAI</div>
              <div className="col-span-3">NIP / ID</div>
              <div className="col-span-2">ROLE AKSES</div>
              <div className="col-span-1 text-center">STATUS</div>
              <div className="col-span-2 text-center">AKSI</div>
            </div>

            {/* Baris pegawai */}
            <div className="divide-y divide-gray-100">
              {paginated.length === 0 ? (
                <div className="py-12 text-center text-gray-400 text-sm">Tidak ada pegawai ditemukan</div>
              ) : paginated.map((p) => (
                <div key={p.id} className={`grid grid-cols-12 gap-2 px-4 py-3.5 items-center hover:bg-gray-50 transition-colors ${!p.aktif ? "opacity-60" : ""}`}>

                  {/* Nama + inisial */}
                  <div className="col-span-4 flex items-center gap-3">
                    <div className={`w-9 h-9 rounded-full ${p.warna} flex items-center justify-center text-white text-xs font-black shrink-0`}>
                      {p.inisial}
                    </div>
                    <span className={`text-sm font-semibold text-gray-800 ${!p.aktif ? "line-through" : ""}`}>{p.nama}</span>
                  </div>

                  {/* NIP */}
                  <div className="col-span-3 text-xs text-gray-500 font-mono">{p.nip}</div>

                  {/* Role */}
                  <div className="col-span-2">
                    <span className={`text-xs font-semibold px-2 py-1 rounded-lg ${ROLE_WARNA[p.role]}`}>{p.role}</span>
                  </div>

                  {/* Status toggle */}
                  <div className="col-span-1 flex justify-center">
                    <button onClick={() => handleToggleAktif(p.id)} title={p.aktif ? "Nonaktifkan" : "Aktifkan"}>
                      {p.aktif
                        ? <svg className="w-5 h-5 text-green-500" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z"/></svg>
                        : <svg className="w-5 h-5 text-red-400" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M10 14l2-2m0 0l2-2m-2 2l-2-2m2 2l2 2m7-2a9 9 0 11-18 0 9 9 0 0118 0z"/></svg>
                      }
                    </button>
                  </div>

                  {/* Aksi */}
                  <div className="col-span-2 flex items-center justify-center gap-1.5">
                    {/* Edit */}
                    <button onClick={() => handleEdit(p)}
                      className="p-1.5 rounded-lg bg-[#DDE5FE] hover:bg-[#1B4EF5] text-[#1B4EF5] hover:text-white transition-all" title="Edit">
                      <svg className="w-3.5 h-3.5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z"/>
                      </svg>
                    </button>
                    {/* Reset password */}
                    <button onClick={() => { setEditPassId(p.id); setEditPassNama(p.nama); setEditPassBaru(""); setShowEditPass(true); }}
                      className="p-1.5 rounded-lg bg-amber-50 hover:bg-amber-500 text-amber-500 hover:text-white transition-all" title="Reset Password">
                      <svg className="w-3.5 h-3.5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M15 7a2 2 0 012 2m4 0a6 6 0 01-7.743 5.743L11 17H9v2H7v2H4a1 1 0 01-1-1v-2.586a1 1 0 01.293-.707l5.964-5.964A6 6 0 1121 9z"/>
                      </svg>
                    </button>
                    {/* Hapus */}
                    <button onClick={() => { setDeleteId(p.id); setShowDelete(true); }}
                      className="p-1.5 rounded-lg bg-red-50 hover:bg-red-500 text-red-500 hover:text-white transition-all" title="Hapus">
                      <svg className="w-3.5 h-3.5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16"/>
                      </svg>
                    </button>
                  </div>
                </div>
              ))}
            </div>

            {/* Footer tabel */}
            <div className="flex items-center justify-between px-4 py-3 border-t border-gray-100">
              <p className="text-xs text-gray-500">
                Menampilkan {Math.min((currentPage-1)*itemsPerPage+1, filtered.length)}–{Math.min(currentPage*itemsPerPage, filtered.length)} dari {filtered.length} staf
              </p>
              <div className="flex items-center gap-1">
                <button onClick={() => setCurrentPage((p) => Math.max(1, p-1))} disabled={currentPage === 1}
                  className="w-7 h-7 flex items-center justify-center rounded-lg border border-gray-200 text-gray-500 hover:border-[#1B4EF5] hover:text-[#1B4EF5] disabled:opacity-40 disabled:cursor-not-allowed transition-all">
                  <svg className="w-3.5 h-3.5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M15 19l-7-7 7-7"/></svg>
                </button>
                {Array.from({ length: totalPages }, (_, i) => i+1).map((p) => (
                  <button key={p} onClick={() => setCurrentPage(p)}
                    className={`w-7 h-7 flex items-center justify-center rounded-lg text-xs font-medium transition-all ${currentPage === p ? "bg-[#1B4EF5] text-white shadow" : "border border-gray-200 text-gray-600 hover:border-[#1B4EF5] hover:text-[#1B4EF5]"}`}>
                    {p}
                  </button>
                ))}
                <button onClick={() => setCurrentPage((p) => Math.min(totalPages, p+1))} disabled={currentPage === totalPages}
                  className="w-7 h-7 flex items-center justify-center rounded-lg border border-gray-200 text-gray-500 hover:border-[#1B4EF5] hover:text-[#1B4EF5] disabled:opacity-40 disabled:cursor-not-allowed transition-all">
                  <svg className="w-3.5 h-3.5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 5l7 7-7 7"/></svg>
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>

      {/* ══════════════════════════════
          MODAL — Tambah / Edit Pegawai
      ══════════════════════════════ */}
      {showModal && (
        <div className="fixed inset-0 bg-black/40 flex items-center justify-center z-50 px-4">
          <div className="bg-white rounded-2xl p-6 shadow-xl w-full max-w-md space-y-4">
            <div className="flex items-center justify-between">
              <h3 className="font-black text-gray-900 text-lg">
                {modalMode === "tambah" ? "Tambah Pegawai" : "Edit Pegawai"}
              </h3>
              <button onClick={() => setShowModal(false)} className="text-gray-400 hover:text-gray-600">
                <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M6 18L18 6M6 6l12 12"/>
                </svg>
              </button>
            </div>

            <div className="space-y-4">
              <div>
                <label className={labelClass}>Nama Lengkap</label>
                <input type="text" placeholder="Masukkan nama lengkap" value={formNama}
                  onChange={(e) => setFormNama(e.target.value)} className={inputClass} />
              </div>
              <div>
                <label className={labelClass}>NIP / ID</label>
                <input type="text" placeholder="NIP atau ID pegawai" value={formNip}
                  onChange={(e) => setFormNip(e.target.value)} className={inputClass} />
              </div>
              <div>
                <label className={labelClass}>Role Akses</label>
                <div className="relative">
                  <select value={formRole} onChange={(e) => setFormRole(e.target.value as RoleType)} className={selectClass}>
                    {ROLE_LIST.map((r) => <option key={r} value={r}>{r}</option>)}
                  </select>
                  <svg className="w-4 h-4 text-gray-400 absolute right-3 top-1/2 -translate-y-1/2 pointer-events-none" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 9l-7 7-7-7"/>
                  </svg>
                </div>
              </div>
              <div>
                <label className={labelClass}>Username</label>
                <input type="text" placeholder="Username untuk login" value={formUsername}
                  onChange={(e) => setFormUsername(e.target.value)} className={inputClass} />
              </div>
              <div>
                <label className={labelClass}>{modalMode === "edit" ? "Password Baru (opsional)" : "Password"}</label>
                <div className="relative">
                  <input type={showFormPass ? "text" : "password"}
                    placeholder={modalMode === "edit" ? "Kosongkan jika tidak diubah" : "Minimal 8 karakter"}
                    value={formPassword} onChange={(e) => setFormPassword(e.target.value)} className={inputClass} />
                  <button type="button" onClick={() => setShowFormPass(!showFormPass)}
                    className="absolute right-3 top-1/2 -translate-y-1/2 text-gray-400 hover:text-gray-600">
                    <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M15 12a3 3 0 11-6 0 3 3 0 016 0z"/>
                      <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z"/>
                    </svg>
                  </button>
                </div>
                <KekuatanPassword password={formPassword} />
              </div>

              {formError && (
                <p className="text-xs text-red-500 bg-red-50 border border-red-100 rounded-lg px-3 py-2">{formError}</p>
              )}
            </div>

            <div className="flex gap-3 pt-1">
              <button onClick={() => setShowModal(false)}
                className="flex-1 border-2 border-gray-200 text-gray-600 font-semibold py-2.5 rounded-xl hover:border-gray-300 transition-all text-sm">
                Batal
              </button>
              <button onClick={handleSimpanModal}
                className="flex-1 bg-[#1B4EF5] hover:bg-[#1540cc] text-white font-bold py-2.5 rounded-xl transition-all text-sm">
                {modalMode === "tambah" ? "Tambah" : "Simpan"}
              </button>
            </div>
          </div>
        </div>
      )}

      {/* ══════════════════════════════
          MODAL — Reset Password Pegawai
      ══════════════════════════════ */}
      {showEditPass && (
        <div className="fixed inset-0 bg-black/40 flex items-center justify-center z-50 px-4">
          <div className="bg-white rounded-2xl p-6 shadow-xl w-full max-w-sm space-y-4">
            <div className="flex items-center gap-3">
              <div className="w-10 h-10 rounded-full bg-amber-100 flex items-center justify-center shrink-0">
                <svg className="w-5 h-5 text-amber-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M15 7a2 2 0 012 2m4 0a6 6 0 01-7.743 5.743L11 17H9v2H7v2H4a1 1 0 01-1-1v-2.586a1 1 0 01.293-.707l5.964-5.964A6 6 0 1121 9z"/>
                </svg>
              </div>
              <div>
                <h3 className="font-bold text-gray-900">Reset Password</h3>
                <p className="text-xs text-gray-500">{editPassNama}</p>
              </div>
            </div>

            <div>
              <label className={labelClass}>Password Baru</label>
              <div className="relative">
                <input type={showEditPassVal ? "text" : "password"}
                  placeholder="Minimal 8 karakter"
                  value={editPassBaru} onChange={(e) => setEditPassBaru(e.target.value)} className={inputClass} />
                <button type="button" onClick={() => setShowEditPassVal(!showEditPassVal)}
                  className="absolute right-3 top-1/2 -translate-y-1/2 text-gray-400 hover:text-gray-600">
                  <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M15 12a3 3 0 11-6 0 3 3 0 016 0z"/>
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z"/>
                  </svg>
                </button>
              </div>
              <KekuatanPassword password={editPassBaru} />
            </div>

            <div className="flex gap-3">
              <button onClick={() => { setShowEditPass(false); setEditPassBaru(""); }}
                className="flex-1 border-2 border-gray-200 text-gray-600 font-semibold py-2.5 rounded-xl text-sm hover:border-gray-300 transition-all">
                Batal
              </button>
              <button onClick={handleSimpanPassPegawai} disabled={editPassBaru.length < 8}
                className="flex-1 bg-amber-500 hover:bg-amber-600 disabled:opacity-50 disabled:cursor-not-allowed text-white font-bold py-2.5 rounded-xl text-sm transition-all">
                Reset Password
              </button>
            </div>
          </div>
        </div>
      )}

      {/* ══════════════════════════════
          MODAL — Konfirmasi Hapus
      ══════════════════════════════ */}
      {showDelete && (
        <div className="fixed inset-0 bg-black/40 flex items-center justify-center z-50 px-4">
          <div className="bg-white rounded-2xl p-6 shadow-xl w-full max-w-sm space-y-4">
            <div className="flex items-center gap-3">
              <div className="w-10 h-10 rounded-full bg-red-100 flex items-center justify-center shrink-0">
                <svg className="w-5 h-5 text-red-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16"/>
                </svg>
              </div>
              <div>
                <h3 className="font-bold text-gray-900">Hapus Pegawai</h3>
                <p className="text-xs text-gray-500">Data yang dihapus tidak bisa dikembalikan.</p>
              </div>
            </div>
            <p className="text-sm text-gray-600">Apakah kamu yakin ingin menghapus pegawai ini?</p>
            <div className="flex gap-3">
              <button onClick={() => { setShowDelete(false); setDeleteId(null); }}
                className="flex-1 border-2 border-gray-200 text-gray-600 font-semibold py-2.5 rounded-xl text-sm hover:border-gray-300 transition-all">
                Batal
              </button>
              <button onClick={handleHapus}
                className="flex-1 bg-red-500 hover:bg-red-600 text-white font-semibold py-2.5 rounded-xl text-sm transition-all">
                Ya, Hapus
              </button>
            </div>
          </div>
        </div>
      )}

    </div>
  );
}