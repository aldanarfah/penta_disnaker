"use client";

import { useState, useMemo } from "react";
import { PieChart, Pie, Cell, Tooltip, ResponsiveContainer, Legend } from "recharts";

// ── Warna ragam disabilitas ──
const WARNA_RAGAM: Record<string, string> = {
  "Fisik":        "#1B4EF5",
  "Mental":       "#8B5CF6",
  "Intelektual":  "#F59E0B",
  "Sensorik":     "#10B981",
};

const WARNA_STATUS: Record<string, string> = {
  "PKWT":  "#1B4EF5",
  "PKWTT": "#93AEFB",
};

const WARNA_PENDIDIKAN: Record<string, string> = {
  "SD":  "#1B4EF5", "SMP": "#3B82F6", "SMA": "#8B5CF6",
  "D3":  "#F59E0B", "S1":  "#10B981", "S2":  "#EF4444", "S3": "#EC4899",
};

const TAHUN_LIST = [2023, 2024, 2025, 2026];
const RAGAM_LIST = ["Fisik", "Mental", "Intelektual", "Sensorik"];
const PENDIDIKAN_LIST = ["SD", "SMP", "SMA", "D3", "S1", "S2", "S3"];
const STATUS_LIST = ["PKWT", "PKWTT"];

// ══════════════════════════════════════════════════════
// DATA DUMMY PERUSAHAAN
// ══════════════════════════════════════════════════════
const rawPerusahaan = [
  // Mustika Jaya
  ...Array.from({ length: 8 },  () => ({ tahun: 2026, perusahaan: "Mustika Jaya",    ragam: "Fisik",       pendidikan: "SMA", statusKepegawaian: "PKWT",  jenisKelamin: "L" })),
  ...Array.from({ length: 5 },  () => ({ tahun: 2026, perusahaan: "Mustika Jaya",    ragam: "Fisik",       pendidikan: "SMA", statusKepegawaian: "PKWT",  jenisKelamin: "P" })),
  ...Array.from({ length: 3 },  () => ({ tahun: 2026, perusahaan: "Mustika Jaya",    ragam: "Mental",      pendidikan: "SMP", statusKepegawaian: "PKWTT", jenisKelamin: "L" })),
  ...Array.from({ length: 2 },  () => ({ tahun: 2026, perusahaan: "Mustika Jaya",    ragam: "Intelektual", pendidikan: "SD",  statusKepegawaian: "PKWTT", jenisKelamin: "P" })),
  ...Array.from({ length: 4 },  () => ({ tahun: 2026, perusahaan: "Mustika Jaya",    ragam: "Sensorik",    pendidikan: "D3",  statusKepegawaian: "PKWT",  jenisKelamin: "L" })),
  // PT Maju Bersama
  ...Array.from({ length: 6 },  () => ({ tahun: 2026, perusahaan: "PT Maju Bersama", ragam: "Fisik",       pendidikan: "S1",  statusKepegawaian: "PKWTT", jenisKelamin: "L" })),
  ...Array.from({ length: 4 },  () => ({ tahun: 2026, perusahaan: "PT Maju Bersama", ragam: "Fisik",       pendidikan: "SMA", statusKepegawaian: "PKWT",  jenisKelamin: "P" })),
  ...Array.from({ length: 3 },  () => ({ tahun: 2026, perusahaan: "PT Maju Bersama", ragam: "Sensorik",    pendidikan: "SMA", statusKepegawaian: "PKWT",  jenisKelamin: "L" })),
  ...Array.from({ length: 2 },  () => ({ tahun: 2026, perusahaan: "PT Maju Bersama", ragam: "Mental",      pendidikan: "D3",  statusKepegawaian: "PKWTT", jenisKelamin: "P" })),
  // Indomaret
  ...Array.from({ length: 10 }, () => ({ tahun: 2026, perusahaan: "Indomaret",       ragam: "Fisik",       pendidikan: "SMA", statusKepegawaian: "PKWT",  jenisKelamin: "L" })),
  ...Array.from({ length: 7 },  () => ({ tahun: 2026, perusahaan: "Indomaret",       ragam: "Fisik",       pendidikan: "SMA", statusKepegawaian: "PKWT",  jenisKelamin: "P" })),
  ...Array.from({ length: 3 },  () => ({ tahun: 2026, perusahaan: "Indomaret",       ragam: "Intelektual", pendidikan: "SMP", statusKepegawaian: "PKWTT", jenisKelamin: "L" })),
  // Alfamart
  ...Array.from({ length: 5 },  () => ({ tahun: 2026, perusahaan: "Alfamart",        ragam: "Fisik",       pendidikan: "SMA", statusKepegawaian: "PKWT",  jenisKelamin: "P" })),
  ...Array.from({ length: 3 },  () => ({ tahun: 2026, perusahaan: "Alfamart",        ragam: "Sensorik",    pendidikan: "S1",  statusKepegawaian: "PKWTT", jenisKelamin: "L" })),
  ...Array.from({ length: 2 },  () => ({ tahun: 2026, perusahaan: "Alfamart",        ragam: "Mental",      pendidikan: "SMP", statusKepegawaian: "PKWT",  jenisKelamin: "P" })),
  // 2025
  ...Array.from({ length: 12 }, () => ({ tahun: 2025, perusahaan: "Mustika Jaya",    ragam: "Fisik",       pendidikan: "SMA", statusKepegawaian: "PKWT",  jenisKelamin: "L" })),
  ...Array.from({ length: 6 },  () => ({ tahun: 2025, perusahaan: "Indomaret",       ragam: "Fisik",       pendidikan: "SMA", statusKepegawaian: "PKWT",  jenisKelamin: "P" })),
  ...Array.from({ length: 4 },  () => ({ tahun: 2025, perusahaan: "PT Maju Bersama", ragam: "Mental",      pendidikan: "D3",  statusKepegawaian: "PKWTT", jenisKelamin: "L" })),
];

// ══════════════════════════════════════════════════════
// DATA DUMMY LANSIA
// ══════════════════════════════════════════════════════
const rawLansia = [
  ...Array.from({ length: 6 },  () => ({ tahun: 2026, ragam: "Fisik",       pendidikan: "SD",  statusKepegawaian: "PKWT",  jenisKelamin: "L" })),
  ...Array.from({ length: 4 },  () => ({ tahun: 2026, ragam: "Fisik",       pendidikan: "SD",  statusKepegawaian: "PKWT",  jenisKelamin: "P" })),
  ...Array.from({ length: 3 },  () => ({ tahun: 2026, ragam: "Mental",      pendidikan: "SMP", statusKepegawaian: "PKWTT", jenisKelamin: "L" })),
  ...Array.from({ length: 5 },  () => ({ tahun: 2026, ragam: "Fisik",       pendidikan: "SMP", statusKepegawaian: "PKWTT", jenisKelamin: "P" })),
  ...Array.from({ length: 2 },  () => ({ tahun: 2026, ragam: "Intelektual", pendidikan: "SD",  statusKepegawaian: "PKWT",  jenisKelamin: "L" })),
  ...Array.from({ length: 4 },  () => ({ tahun: 2026, ragam: "Sensorik",    pendidikan: "SMA", statusKepegawaian: "PKWT",  jenisKelamin: "P" })),
  ...Array.from({ length: 3 },  () => ({ tahun: 2026, ragam: "Fisik",       pendidikan: "SMA", statusKepegawaian: "PKWTT", jenisKelamin: "L" })),
  ...Array.from({ length: 8 },  () => ({ tahun: 2025, ragam: "Fisik",       pendidikan: "SD",  statusKepegawaian: "PKWT",  jenisKelamin: "L" })),
  ...Array.from({ length: 5 },  () => ({ tahun: 2025, ragam: "Mental",      pendidikan: "SMP", statusKepegawaian: "PKWTT", jenisKelamin: "P" })),
  ...Array.from({ length: 3 },  () => ({ tahun: 2025, ragam: "Sensorik",    pendidikan: "SMA", statusKepegawaian: "PKWT",  jenisKelamin: "L" })),
];

// ── Custom Tooltip ──
const CustomTooltip = ({ active, payload }: any) => {
  if (active && payload && payload.length) {
    const { name, value, payload: p } = payload[0];
    return (
      <div className="bg-white border border-gray-100 rounded-xl shadow-lg px-4 py-3 text-sm min-w-[160px]">
        <p className="font-bold text-gray-800 mb-1">{name}</p>
        <p className="text-gray-600">Jumlah: <span className="font-bold text-gray-900">{value} orang</span></p>
        <p className="text-gray-600">Persentase: <span className="font-bold" style={{ color: p.fill }}>{p.persen}%</span></p>
        <p className="text-gray-600">L: <span className="font-bold">{p.l}</span> &nbsp; P: <span className="font-bold">{p.p}</span></p>
      </div>
    );
  }
  return null;
};

const renderLabel = ({ cx, cy, midAngle, innerRadius, outerRadius, persen }: any) => {
  if (persen < 5) return null;
  const RADIAN = Math.PI / 180;
  const radius = innerRadius + (outerRadius - innerRadius) * 0.55;
  const x = cx + radius * Math.cos(-midAngle * RADIAN);
  const y = cy + radius * Math.sin(-midAngle * RADIAN);
  return (
    <text x={x} y={y} fill="white" textAnchor="middle" dominantBaseline="central" fontSize={12} fontWeight={700}>
      {persen}%
    </text>
  );
};

// ── Komponen Pie Chart + Tabel ──
function PieSection({
  title, subtitle, data, warna, total,
}: {
  title: string;
  subtitle: string;
  data: { name: string; value: number; l: number; p: number; persen: number; fill: string }[];
  warna: Record<string, string>;
  total: number;
}) {
  return (
    <div className="bg-white rounded-2xl p-6 border border-gray-100 shadow-sm space-y-4">
      <div>
        <h3 className="font-bold text-gray-900 text-base">{title}</h3>
        <p className="text-xs text-gray-400 mt-0.5">{subtitle}</p>
      </div>

      {total === 0 ? (
        <div className="flex items-center justify-center h-48 text-gray-400 text-sm">Tidak ada data</div>
      ) : (
        <>
          <ResponsiveContainer width="100%" height={240}>
            <PieChart>
              <Pie
                data={data}
                cx="50%" cy="50%"
                outerRadius={95}
                paddingAngle={3}
                dataKey="value"
                labelLine={false}
                label={renderLabel}
              >
                {data.map((entry, i) => (
                  <Cell key={i} fill={entry.fill} />
                ))}
              </Pie>
              <Tooltip content={<CustomTooltip />} />
              <Legend iconType="circle" iconSize={9} wrapperStyle={{ fontSize: 11 }} />
            </PieChart>
          </ResponsiveContainer>

          {/* Tabel ringkasan */}
          <table className="w-full text-xs">
            <thead>
              <tr className="bg-[#DDE5FE] text-gray-700">
                <th className="text-left px-3 py-2 font-semibold rounded-tl-xl">Kategori</th>
                <th className="text-center px-3 py-2 font-semibold">L</th>
                <th className="text-center px-3 py-2 font-semibold">P</th>
                <th className="text-center px-3 py-2 font-semibold">Jumlah</th>
                <th className="text-center px-3 py-2 font-semibold rounded-tr-xl">%</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-gray-100">
              {data.map((row) => (
                <tr key={row.name} className="hover:bg-gray-50">
                  <td className="px-3 py-2">
                    <div className="flex items-center gap-2">
                      <div className="w-2.5 h-2.5 rounded-full" style={{ background: row.fill }} />
                      <span className="font-medium text-gray-700">{row.name}</span>
                    </div>
                  </td>
                  <td className="px-3 py-2 text-center font-bold text-[#1B4EF5]">{row.l}</td>
                  <td className="px-3 py-2 text-center font-bold text-[#93AEFB]">{row.p}</td>
                  <td className="px-3 py-2 text-center font-black text-gray-900">{row.value}</td>
                  <td className="px-3 py-2 text-center">
                    <span className="px-2 py-0.5 rounded-full text-xs font-bold" style={{ background: `${row.fill}20`, color: row.fill }}>
                      {row.persen}%
                    </span>
                  </td>
                </tr>
              ))}
            </tbody>
            <tfoot>
              <tr className="bg-[#DDE5FE] font-bold">
                <td className="px-3 py-2 text-[#1B4EF5] rounded-bl-xl">TOTAL</td>
                <td className="px-3 py-2 text-center text-[#1B4EF5]">{data.reduce((s, d) => s + d.l, 0)}</td>
                <td className="px-3 py-2 text-center text-[#1B4EF5]">{data.reduce((s, d) => s + d.p, 0)}</td>
                <td className="px-3 py-2 text-center text-gray-900">{total}</td>
                <td className="px-3 py-2 text-center text-gray-900 rounded-br-xl">100%</td>
              </tr>
            </tfoot>
          </table>
        </>
      )}
    </div>
  );
}

// ── Helper hitung pie data ──
function hitungPie(
  rows: typeof rawPerusahaan,
  groupKey: keyof typeof rawPerusahaan[0],
  warnaMap: Record<string, string>,
  keyList: string[],
) {
  const total = rows.length;
  return keyList
    .map((key) => {
      const subset = rows.filter((d) => d[groupKey] === key);
      const l = subset.filter((d) => d.jenisKelamin === "L").length;
      const p = subset.filter((d) => d.jenisKelamin === "P").length;
      const value = subset.length;
      const persen = total > 0 ? parseFloat(((value / total) * 100).toFixed(1)) : 0;
      return { name: key, value, l, p, persen, fill: warnaMap[key] ?? "#CBD5E1" };
    })
    .filter((d) => d.value > 0);
}

export default function RekapDisabilitasPage() {
  const [tab, setTab] = useState<"perusahaan" | "lansia">("perusahaan");
  const [tahun, setTahun] = useState(2026);

  // ── Filter Perusahaan ──
  const [filterPerusahaan, setFilterPerusahaan] = useState("Semua");
  const [filterPendidikanP, setFilterPendidikanP] = useState("Semua");
  const [filterStatusP, setFilterStatusP] = useState("Semua");

  // ── Filter Lansia ──
  const [filterStatusL, setFilterStatusL] = useState("Semua");
  const [filterPendidikanL, setFilterPendidikanL] = useState("Semua");

  // ── Data Perusahaan ──
  const perusahaanList = useMemo(() => {
    const set = new Set(rawPerusahaan.map((d) => d.perusahaan));
    return Array.from(set).sort();
  }, []);

  const filteredPerusahaan = useMemo(() =>
    rawPerusahaan.filter((d) => {
      if (d.tahun !== tahun) return false;
      if (filterPerusahaan !== "Semua" && d.perusahaan !== filterPerusahaan) return false;
      if (filterPendidikanP !== "Semua" && d.pendidikan !== filterPendidikanP) return false;
      if (filterStatusP !== "Semua" && d.statusKepegawaian !== filterStatusP) return false;
      return true;
    }), [tahun, filterPerusahaan, filterPendidikanP, filterStatusP]);

  const totalPerusahaan = filteredPerusahaan.length;

  const pieRagamP = useMemo(() =>
    hitungPie(filteredPerusahaan, "ragam", WARNA_RAGAM, RAGAM_LIST),
    [filteredPerusahaan]);

  const piePendidikanP = useMemo(() =>
    hitungPie(filteredPerusahaan, "pendidikan", WARNA_PENDIDIKAN, PENDIDIKAN_LIST),
    [filteredPerusahaan]);

  const pieStatusP = useMemo(() =>
    hitungPie(filteredPerusahaan, "statusKepegawaian", WARNA_STATUS, STATUS_LIST),
    [filteredPerusahaan]);

  // ── Data Lansia ──
  const filteredLansia = useMemo(() =>
    rawLansia.filter((d) => {
      if (d.tahun !== tahun) return false;
      if (filterStatusL !== "Semua" && d.statusKepegawaian !== filterStatusL) return false;
      if (filterPendidikanL !== "Semua" && d.pendidikan !== filterPendidikanL) return false;
      return true;
    }), [tahun, filterStatusL, filterPendidikanL]);

  const totalLansia = filteredLansia.length;

  const pieRagamL = useMemo(() =>
    hitungPie(filteredLansia as any, "ragam", WARNA_RAGAM, RAGAM_LIST),
    [filteredLansia]);

  const pieStatusL = useMemo(() =>
    hitungPie(filteredLansia as any, "statusKepegawaian", WARNA_STATUS, STATUS_LIST),
    [filteredLansia]);

  const piePendidikanL = useMemo(() =>
    hitungPie(filteredLansia as any, "pendidikan", WARNA_PENDIDIKAN, PENDIDIKAN_LIST),
    [filteredLansia]);

  const selectClass = "border border-gray-200 rounded-xl px-3 py-2 text-sm bg-white focus:outline-none focus:ring-2 focus:ring-[#1B4EF5]/30 focus:border-[#1B4EF5] cursor-pointer appearance-none pr-8";

  const SelectWrap = ({ children }: { children: React.ReactNode }) => (
    <div className="relative">
      {children}
      <svg className="w-4 h-4 text-gray-400 absolute right-2 top-1/2 -translate-y-1/2 pointer-events-none" fill="none" stroke="currentColor" viewBox="0 0 24 24">
        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 9l-7 7-7-7" />
      </svg>
    </div>
  );

  return (
    <div className="space-y-6 pb-10">

      {/* ── Header ── */}
      <div className="flex items-start justify-between flex-wrap gap-4">
        <div>
          <p className="text-sm text-gray-500">Rekapitulasi Data</p>
          <h1 className="text-2xl font-black text-gray-900">Disabilitas</h1>
          <p className="text-sm text-gray-400 mt-1">
            Rekap data tenaga kerja disabilitas — {tahun}
          </p>
        </div>
        <div className="flex items-center gap-2 bg-white border border-gray-200 rounded-xl px-3 py-2">
          <svg className="w-4 h-4 text-[#1B4EF5]" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z" />
          </svg>
          <span className="text-xs text-gray-500 font-medium">Tahun</span>
          <SelectWrap>
            <select value={tahun} onChange={(e) => setTahun(Number(e.target.value))} className={selectClass}>
              {TAHUN_LIST.map((t) => <option key={t} value={t}>{t}</option>)}
            </select>
          </SelectWrap>
        </div>
      </div>

      {/* ── Tab Perusahaan / Lansia ── */}
      <div className="flex gap-2 bg-white border border-gray-100 rounded-2xl p-1.5 w-fit shadow-sm">
        {(["perusahaan", "lansia"] as const).map((t) => (
          <button
            key={t}
            onClick={() => setTab(t)}
            className={`px-6 py-2.5 rounded-xl text-sm font-bold transition-all ${
              tab === t
                ? "bg-[#1B4EF5] text-white shadow"
                : "text-gray-500 hover:text-gray-700"
            }`}
          >
            {t === "perusahaan" ? "Tenaga Kerja Perusahaan" : "Lansia"}
          </button>
        ))}
      </div>

      {/* ════════════════════════════
          TAB PERUSAHAAN
      ════════════════════════════ */}
      {tab === "perusahaan" && (
        <div className="space-y-5">

          {/* Filter */}
          <div className="bg-white rounded-2xl p-4 border border-gray-100 shadow-sm flex items-center gap-4 flex-wrap">
            <span className="text-sm font-semibold text-gray-600">Filter:</span>

            <div className="flex items-center gap-2">
              <span className="text-xs text-gray-500">Perusahaan</span>
              <SelectWrap>
                <select value={filterPerusahaan} onChange={(e) => setFilterPerusahaan(e.target.value)} className={`${selectClass} text-xs`}>
                  <option value="Semua">Semua Perusahaan</option>
                  {perusahaanList.map((p) => <option key={p} value={p}>{p}</option>)}
                </select>
              </SelectWrap>
            </div>

            <div className="flex items-center gap-2">
              <span className="text-xs text-gray-500">Pendidikan</span>
              <SelectWrap>
                <select value={filterPendidikanP} onChange={(e) => setFilterPendidikanP(e.target.value)} className={`${selectClass} text-xs`}>
                  <option value="Semua">Semua</option>
                  {PENDIDIKAN_LIST.map((p) => <option key={p} value={p}>{p}</option>)}
                </select>
              </SelectWrap>
            </div>

            <div className="flex items-center gap-2">
              <span className="text-xs text-gray-500">Status Kepegawaian</span>
              <SelectWrap>
                <select value={filterStatusP} onChange={(e) => setFilterStatusP(e.target.value)} className={`${selectClass} text-xs`}>
                  <option value="Semua">Semua</option>
                  {STATUS_LIST.map((s) => <option key={s} value={s}>{s}</option>)}
                </select>
              </SelectWrap>
            </div>

            {/* Reset */}
            {(filterPerusahaan !== "Semua" || filterPendidikanP !== "Semua" || filterStatusP !== "Semua") && (
              <button
                onClick={() => { setFilterPerusahaan("Semua"); setFilterPendidikanP("Semua"); setFilterStatusP("Semua"); }}
                className="text-xs text-red-500 hover:text-red-600 font-semibold flex items-center gap-1"
              >
                <svg className="w-3.5 h-3.5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M6 18L18 6M6 6l12 12" />
                </svg>
                Reset
              </button>
            )}

            <div className="ml-auto bg-[#DDE5FE] rounded-xl px-4 py-2 text-xs font-bold text-[#1B4EF5]">
              Total: {totalPerusahaan} orang
            </div>
          </div>

          {/* Summary Cards Perusahaan */}
          <div className="grid grid-cols-4 gap-4">
            <div className="bg-white rounded-2xl p-5 border border-gray-100 shadow-sm flex items-center gap-4 relative overflow-hidden">
              <div className="absolute right-0 top-0 w-20 h-20 rounded-full bg-[#DDE5FE] -translate-y-4 translate-x-4 opacity-50" />
              <div className="w-11 h-11 rounded-full bg-[#DDE5FE] flex items-center justify-center shrink-0 z-10">
                <svg className="w-5 h-5 text-[#1B4EF5]" fill="currentColor" viewBox="0 0 24 24">
                  <path d="M16 11c1.66 0 2.99-1.34 2.99-3S17.66 5 16 5c-1.66 0-3 1.34-3 3s1.34 3 3 3zm-8 0c1.66 0 2.99-1.34 2.99-3S9.66 5 8 5C6.34 5 5 6.34 5 8s1.34 3 3 3zm0 2c-2.33 0-7 1.17-7 3.5V19h14v-2.5c0-2.33-4.67-3.5-7-3.5zm8 0c-.29 0-.62.02-.97.05 1.16.84 1.97 1.97 1.97 3.45V19h6v-2.5c0-2.33-4.67-3.5-7-3.5z" />
                </svg>
              </div>
              <div className="z-10">
                <p className="text-xs text-gray-500">Total</p>
                <p className="text-2xl font-black text-gray-900">{totalPerusahaan}</p>
                <p className="text-xs text-gray-400">orang</p>
              </div>
            </div>
            {RAGAM_LIST.map((ragam) => {
              const count = filteredPerusahaan.filter((d) => d.ragam === ragam).length;
              const persen = totalPerusahaan > 0 ? ((count / totalPerusahaan) * 100).toFixed(1) : "0";
              return (
                <div key={ragam} className="bg-white rounded-2xl p-5 border border-gray-100 shadow-sm relative overflow-hidden">
                  <div className="absolute right-0 top-0 w-20 h-20 rounded-full -translate-y-4 translate-x-4 opacity-20" style={{ background: WARNA_RAGAM[ragam] }} />
                  <div className="flex items-center gap-2 mb-2">
                    <div className="w-3 h-3 rounded-full" style={{ background: WARNA_RAGAM[ragam] }} />
                    <p className="text-xs text-gray-500 font-medium">{ragam}</p>
                  </div>
                  <p className="text-2xl font-black text-gray-900">{count} <span className="text-sm font-normal text-gray-400">orang</span></p>
                  <p className="text-xs font-bold mt-1" style={{ color: WARNA_RAGAM[ragam] }}>{persen}%</p>
                </div>
              );
            })}
          </div>

          {/* 3 Pie Chart Perusahaan */}
          <div className="grid grid-cols-3 gap-4">
            <PieSection
              title="Ragam Disabilitas"
              subtitle="Distribusi berdasarkan ragam disabilitas"
              data={pieRagamP}
              warna={WARNA_RAGAM}
              total={totalPerusahaan}
            />
            <PieSection
              title="Pendidikan Terakhir"
              subtitle="Distribusi berdasarkan tingkat pendidikan"
              data={piePendidikanP}
              warna={WARNA_PENDIDIKAN}
              total={totalPerusahaan}
            />
            <PieSection
              title="Status Kepegawaian"
              subtitle="Distribusi PKWT dan PKWTT"
              data={pieStatusP}
              warna={WARNA_STATUS}
              total={totalPerusahaan}
            />
          </div>
        </div>
      )}

      {/* ════════════════════════════
          TAB LANSIA
      ════════════════════════════ */}
      {tab === "lansia" && (
        <div className="space-y-5">

          {/* Filter Lansia */}
          <div className="bg-white rounded-2xl p-4 border border-gray-100 shadow-sm flex items-center gap-4 flex-wrap">
            <span className="text-sm font-semibold text-gray-600">Filter:</span>

            <div className="flex items-center gap-2">
              <span className="text-xs text-gray-500">Status Kepegawaian</span>
              <SelectWrap>
                <select value={filterStatusL} onChange={(e) => setFilterStatusL(e.target.value)} className={`${selectClass} text-xs`}>
                  <option value="Semua">Semua</option>
                  {STATUS_LIST.map((s) => <option key={s} value={s}>{s}</option>)}
                </select>
              </SelectWrap>
            </div>

            <div className="flex items-center gap-2">
              <span className="text-xs text-gray-500">Pendidikan</span>
              <SelectWrap>
                <select value={filterPendidikanL} onChange={(e) => setFilterPendidikanL(e.target.value)} className={`${selectClass} text-xs`}>
                  <option value="Semua">Semua</option>
                  {PENDIDIKAN_LIST.map((p) => <option key={p} value={p}>{p}</option>)}
                </select>
              </SelectWrap>
            </div>

            {(filterStatusL !== "Semua" || filterPendidikanL !== "Semua") && (
              <button
                onClick={() => { setFilterStatusL("Semua"); setFilterPendidikanL("Semua"); }}
                className="text-xs text-red-500 hover:text-red-600 font-semibold flex items-center gap-1"
              >
                <svg className="w-3.5 h-3.5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M6 18L18 6M6 6l12 12" />
                </svg>
                Reset
              </button>
            )}

            <div className="ml-auto bg-[#DDE5FE] rounded-xl px-4 py-2 text-xs font-bold text-[#1B4EF5]">
              Total: {totalLansia} orang
            </div>
          </div>

          {/* Summary Cards Lansia */}
          <div className="grid grid-cols-4 gap-4">
            <div className="bg-white rounded-2xl p-5 border border-gray-100 shadow-sm flex items-center gap-4 relative overflow-hidden">
              <div className="absolute right-0 top-0 w-20 h-20 rounded-full bg-[#DDE5FE] -translate-y-4 translate-x-4 opacity-50" />
              <div className="w-11 h-11 rounded-full bg-[#DDE5FE] flex items-center justify-center shrink-0 z-10">
                <svg className="w-5 h-5 text-[#1B4EF5]" fill="currentColor" viewBox="0 0 24 24">
                  <path d="M11.5 2C6.81 2 3 5.81 3 10.5S6.81 19 11.5 19h.5v3c4.86-2.34 8-7 8-11.5C20 5.81 16.19 2 11.5 2zm1 14.5h-2v-2h2v2zm0-4h-2c0-3.25 3-3 3-5 0-1.1-.9-2-2-2s-2 .9-2 2h-2c0-2.21 1.79-4 4-4s4 1.79 4 4c0 2.5-3 2.75-3 5z" />
                </svg>
              </div>
              <div className="z-10">
                <p className="text-xs text-gray-500">Total Lansia</p>
                <p className="text-2xl font-black text-gray-900">{totalLansia}</p>
                <p className="text-xs text-gray-400">orang</p>
              </div>
            </div>
            {STATUS_LIST.map((status) => {
              const count = filteredLansia.filter((d) => d.statusKepegawaian === status).length;
              const persen = totalLansia > 0 ? ((count / totalLansia) * 100).toFixed(1) : "0";
              return (
                <div key={status} className="bg-white rounded-2xl p-5 border border-gray-100 shadow-sm relative overflow-hidden">
                  <div className="absolute right-0 top-0 w-20 h-20 rounded-full -translate-y-4 translate-x-4 opacity-20" style={{ background: WARNA_STATUS[status] }} />
                  <div className="flex items-center gap-2 mb-2">
                    <div className="w-3 h-3 rounded-full" style={{ background: WARNA_STATUS[status] }} />
                    <p className="text-xs text-gray-500 font-medium">{status}</p>
                  </div>
                  <p className="text-2xl font-black text-gray-900">{count} <span className="text-sm font-normal text-gray-400">orang</span></p>
                  <p className="text-xs font-bold mt-1" style={{ color: WARNA_STATUS[status] }}>{persen}%</p>
                </div>
              );
            })}
            {/* Card ragam terbanyak */}
            <div className="bg-white rounded-2xl p-5 border border-gray-100 shadow-sm relative overflow-hidden">
              <div className="absolute right-0 top-0 w-20 h-20 rounded-full bg-purple-50 -translate-y-4 translate-x-4 opacity-50" />
              <div className="flex items-center gap-2 mb-2">
                <div className="w-3 h-3 rounded-full bg-purple-400" />
                <p className="text-xs text-gray-500 font-medium">Ragam Terbanyak</p>
              </div>
              {(() => {
                const ragamCount = RAGAM_LIST.map((r) => ({ r, n: filteredLansia.filter((d) => d.ragam === r).length }));
                const top = ragamCount.sort((a, b) => b.n - a.n)[0];
                return top && top.n > 0 ? (
                  <>
                    <p className="text-lg font-black text-gray-900">{top.r}</p>
                    <p className="text-xs font-bold mt-1" style={{ color: WARNA_RAGAM[top.r] }}>{top.n} orang</p>
                  </>
                ) : <p className="text-sm text-gray-400">-</p>;
              })()}
            </div>
          </div>

          {/* 3 Pie Chart Lansia */}
          <div className="grid grid-cols-3 gap-4">
            <PieSection
              title="Ragam Disabilitas"
              subtitle="Distribusi ragam disabilitas lansia"
              data={pieRagamL}
              warna={WARNA_RAGAM}
              total={totalLansia}
            />
            <PieSection
              title="Status Kepegawaian"
              subtitle="Distribusi PKWT dan PKWTT lansia"
              data={pieStatusL}
              warna={WARNA_STATUS}
              total={totalLansia}
            />
            <PieSection
              title="Pendidikan Terakhir"
              subtitle="Distribusi berdasarkan tingkat pendidikan"
              data={piePendidikanL}
              warna={WARNA_PENDIDIKAN}
              total={totalLansia}
            />
          </div>
        </div>
      )}
    </div>
  );
}