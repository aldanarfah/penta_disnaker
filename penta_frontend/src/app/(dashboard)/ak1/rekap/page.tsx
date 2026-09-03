"use client";

import { useState, useMemo } from "react";
import {
  LineChart, Line, BarChart, Bar, XAxis, YAxis, CartesianGrid,
  Tooltip, Legend, ResponsiveContainer, PieChart, Pie, Cell, LabelList,
} from "recharts";

// ── Warna ──
const BIRU = "#1B4EF5";
const BIRU_MUDA = "#93AEFB";
const WARNA_KAWIN: Record<string, string> = {
  "Kawin": "#1B4EF5", "Belum Kawin": "#93AEFB",
  "Janda": "#F59E0B", "Duda": "#10B981",
};
const WARNA_PENDIDIKAN: Record<string, string> = {
  "Tidak/Belum Sekolah": "#94A3B8", "SD": "#1B4EF5", "SMP": "#3B82F6",
  "SMA": "#8B5CF6", "SMK": "#F59E0B", "Diploma": "#10B981",
  "S1": "#EF4444", "S2": "#EC4899", "S3": "#F97316",
};

const TAHUN_LIST = [2023, 2024, 2025, 2026];
const BULAN_LABEL: Record<number, string> = {
  1: "Januari", 2: "Februari", 3: "Maret", 4: "April",
  5: "Mei", 6: "Juni", 7: "Juli", 8: "Agustus",
  9: "September", 10: "Oktober", 11: "November", 12: "Desember",
};
const BULAN_SHORT: Record<number, string> = {
  1: "Jan", 2: "Feb", 3: "Mar", 4: "Apr", 5: "Mei", 6: "Jun",
  7: "Jul", 8: "Agu", 9: "Sep", 10: "Okt", 11: "Nov", 12: "Des",
};
const TRIWULAN_LIST = [
  { label: "Triwulan I (Jan–Mar)",   value: 1, bulan: [1, 2, 3] },
  { label: "Triwulan II (Apr–Jun)",  value: 2, bulan: [4, 5, 6] },
  { label: "Triwulan III (Jul–Sep)", value: 3, bulan: [7, 8, 9] },
  { label: "Triwulan IV (Okt–Des)",  value: 4, bulan: [10, 11, 12] },
];
const PENDIDIKAN_LIST = [
  "SD", "SMP", "SMA", "SMK", "D3", "D4", "S1", "S2", "S3",
];
const KAWIN_LIST = ["Kawin", "Belum Kawin", "Janda", "Duda"];
const TUJUAN_DALAM = ["Lumajang", "Surabaya", "Malang", "Jakarta", "Bandung", "Indomaret", "Alfamart", "PT Maju Bersama"];
const TUJUAN_LUAR  = ["Taiwan", "Singapura", "Jepang", "Hong Kong", "Arab Saudi", "Malaysia", "Korea", "Brunei"];

// ══════════════════════════════════════════════════════
// DATA DUMMY
// ══════════════════════════════════════════════════════
type Row = {
  tahun: number; bulan: number;
  jenisKelamin: "L" | "P";
  pendidikan: string;
  statusPerkawinan: string;
  tujuan: string;
  kategoriTujuan: "Dalam Negeri" | "Luar Negeri";
};

function buat(n: number, obj: Omit<Row, never>): Row[] {
  return Array.from({ length: n }, () => ({ ...obj }));
}

const rawData: Row[] = [
  // 2026
  ...buat(12, { tahun:2026,bulan:1, jenisKelamin:"L", pendidikan:"SMA",     statusPerkawinan:"Belum Kawin", tujuan:"Lumajang",   kategoriTujuan:"Dalam Negeri" }),
  ...buat(8,  { tahun:2026,bulan:1, jenisKelamin:"P", pendidikan:"SMA",     statusPerkawinan:"Belum Kawin", tujuan:"Indomaret",  kategoriTujuan:"Dalam Negeri" }),
  ...buat(5,  { tahun:2026,bulan:1, jenisKelamin:"L", pendidikan:"SMK",     statusPerkawinan:"Kawin",       tujuan:"Taiwan",     kategoriTujuan:"Luar Negeri"  }),
  ...buat(7,  { tahun:2026,bulan:1, jenisKelamin:"P", pendidikan:"SD",      statusPerkawinan:"Janda",       tujuan:"Singapura",  kategoriTujuan:"Luar Negeri"  }),
  ...buat(3,  { tahun:2026,bulan:1, jenisKelamin:"L", pendidikan:"S1",      statusPerkawinan:"Kawin",       tujuan:"Jakarta",    kategoriTujuan:"Dalam Negeri" }),
  ...buat(4,  { tahun:2026,bulan:1, jenisKelamin:"P", pendidikan:"Diploma", statusPerkawinan:"Belum Kawin", tujuan:"Surabaya",   kategoriTujuan:"Dalam Negeri" }),

  ...buat(10, { tahun:2026,bulan:2, jenisKelamin:"L", pendidikan:"SMA",     statusPerkawinan:"Belum Kawin", tujuan:"Lumajang",   kategoriTujuan:"Dalam Negeri" }),
  ...buat(9,  { tahun:2026,bulan:2, jenisKelamin:"P", pendidikan:"SMP",     statusPerkawinan:"Kawin",       tujuan:"Indomaret",  kategoriTujuan:"Dalam Negeri" }),
  ...buat(6,  { tahun:2026,bulan:2, jenisKelamin:"L", pendidikan:"SMK",     statusPerkawinan:"Belum Kawin", tujuan:"Taiwan",     kategoriTujuan:"Luar Negeri"  }),
  ...buat(8,  { tahun:2026,bulan:2, jenisKelamin:"P", pendidikan:"SMA",     statusPerkawinan:"Janda",       tujuan:"Hong Kong",  kategoriTujuan:"Luar Negeri"  }),
  ...buat(4,  { tahun:2026,bulan:2, jenisKelamin:"L", pendidikan:"S1",      statusPerkawinan:"Kawin",       tujuan:"Surabaya",   kategoriTujuan:"Dalam Negeri" }),
  ...buat(3,  { tahun:2026,bulan:2, jenisKelamin:"P", pendidikan:"S1",      statusPerkawinan:"Belum Kawin", tujuan:"Jakarta",    kategoriTujuan:"Dalam Negeri" }),

  ...buat(15, { tahun:2026,bulan:3, jenisKelamin:"L", pendidikan:"SMA",     statusPerkawinan:"Kawin",       tujuan:"Malang",     kategoriTujuan:"Dalam Negeri" }),
  ...buat(11, { tahun:2026,bulan:3, jenisKelamin:"P", pendidikan:"SMA",     statusPerkawinan:"Belum Kawin", tujuan:"Alfamart",   kategoriTujuan:"Dalam Negeri" }),
  ...buat(7,  { tahun:2026,bulan:3, jenisKelamin:"L", pendidikan:"SD",      statusPerkawinan:"Duda",        tujuan:"Jepang",     kategoriTujuan:"Luar Negeri"  }),
  ...buat(9,  { tahun:2026,bulan:3, jenisKelamin:"P", pendidikan:"Diploma", statusPerkawinan:"Kawin",       tujuan:"Taiwan",     kategoriTujuan:"Luar Negeri"  }),

  ...buat(8,  { tahun:2026,bulan:4, jenisKelamin:"L", pendidikan:"SMP",     statusPerkawinan:"Belum Kawin", tujuan:"Lumajang",   kategoriTujuan:"Dalam Negeri" }),
  ...buat(6,  { tahun:2026,bulan:4, jenisKelamin:"P", pendidikan:"SMA",     statusPerkawinan:"Janda",       tujuan:"Singapura",  kategoriTujuan:"Luar Negeri"  }),
  ...buat(5,  { tahun:2026,bulan:4, jenisKelamin:"L", pendidikan:"S1",      statusPerkawinan:"Kawin",       tujuan:"Surabaya",   kategoriTujuan:"Dalam Negeri" }),
  ...buat(7,  { tahun:2026,bulan:4, jenisKelamin:"P", pendidikan:"SMK",     statusPerkawinan:"Belum Kawin", tujuan:"Korea",      kategoriTujuan:"Luar Negeri"  }),

  ...buat(13, { tahun:2026,bulan:5, jenisKelamin:"L", pendidikan:"SMA",     statusPerkawinan:"Kawin",       tujuan:"Jakarta",    kategoriTujuan:"Dalam Negeri" }),
  ...buat(10, { tahun:2026,bulan:5, jenisKelamin:"P", pendidikan:"SMP",     statusPerkawinan:"Belum Kawin", tujuan:"Taiwan",     kategoriTujuan:"Luar Negeri"  }),
  ...buat(4,  { tahun:2026,bulan:5, jenisKelamin:"L", pendidikan:"Diploma", statusPerkawinan:"Duda",        tujuan:"Malang",     kategoriTujuan:"Dalam Negeri" }),
  ...buat(6,  { tahun:2026,bulan:5, jenisKelamin:"P", pendidikan:"S1",      statusPerkawinan:"Kawin",       tujuan:"Arab Saudi", kategoriTujuan:"Luar Negeri"  }),

  ...buat(11, { tahun:2026,bulan:6, jenisKelamin:"L", pendidikan:"SMK",     statusPerkawinan:"Belum Kawin", tujuan:"Bandung",    kategoriTujuan:"Dalam Negeri" }),
  ...buat(9,  { tahun:2026,bulan:6, jenisKelamin:"P", pendidikan:"SMA",     statusPerkawinan:"Kawin",       tujuan:"Singapura",  kategoriTujuan:"Luar Negeri"  }),
  ...buat(6,  { tahun:2026,bulan:6, jenisKelamin:"L", pendidikan:"SD",      statusPerkawinan:"Janda",       tujuan:"Lumajang",   kategoriTujuan:"Dalam Negeri" }),
  ...buat(5,  { tahun:2026,bulan:6, jenisKelamin:"P", pendidikan:"SMP",     statusPerkawinan:"Belum Kawin", tujuan:"Malaysia",   kategoriTujuan:"Luar Negeri"  }),

  ...buat(18, { tahun:2026,bulan:7, jenisKelamin:"L", pendidikan:"SMA",     statusPerkawinan:"Kawin",       tujuan:"Jakarta",    kategoriTujuan:"Dalam Negeri" }),
  ...buat(14, { tahun:2026,bulan:7, jenisKelamin:"P", pendidikan:"SMA",     statusPerkawinan:"Belum Kawin", tujuan:"Taiwan",     kategoriTujuan:"Luar Negeri"  }),
  ...buat(5,  { tahun:2026,bulan:7, jenisKelamin:"L", pendidikan:"S1",      statusPerkawinan:"Duda",        tujuan:"Surabaya",   kategoriTujuan:"Dalam Negeri" }),
  ...buat(8,  { tahun:2026,bulan:7, jenisKelamin:"P", pendidikan:"Diploma", statusPerkawinan:"Kawin",       tujuan:"Jepang",     kategoriTujuan:"Luar Negeri"  }),

  ...buat(14, { tahun:2026,bulan:8, jenisKelamin:"L", pendidikan:"SMP",     statusPerkawinan:"Belum Kawin", tujuan:"Malang",     kategoriTujuan:"Dalam Negeri" }),
  ...buat(12, { tahun:2026,bulan:8, jenisKelamin:"P", pendidikan:"SMK",     statusPerkawinan:"Janda",       tujuan:"Hong Kong",  kategoriTujuan:"Luar Negeri"  }),
  ...buat(6,  { tahun:2026,bulan:8, jenisKelamin:"L", pendidikan:"SMA",     statusPerkawinan:"Kawin",       tujuan:"Bandung",    kategoriTujuan:"Dalam Negeri" }),
  ...buat(4,  { tahun:2026,bulan:8, jenisKelamin:"P", pendidikan:"S1",      statusPerkawinan:"Belum Kawin", tujuan:"Brunei",     kategoriTujuan:"Luar Negeri"  }),

  // 2025 sample
  ...buat(20, { tahun:2025,bulan:1, jenisKelamin:"L", pendidikan:"SMA",     statusPerkawinan:"Kawin",       tujuan:"Lumajang",   kategoriTujuan:"Dalam Negeri" }),
  ...buat(15, { tahun:2025,bulan:1, jenisKelamin:"P", pendidikan:"SMP",     statusPerkawinan:"Belum Kawin", tujuan:"Indomaret",  kategoriTujuan:"Dalam Negeri" }),
  ...buat(10, { tahun:2025,bulan:2, jenisKelamin:"L", pendidikan:"SMK",     statusPerkawinan:"Duda",        tujuan:"Taiwan",     kategoriTujuan:"Luar Negeri"  }),
  ...buat(12, { tahun:2025,bulan:3, jenisKelamin:"P", pendidikan:"S1",      statusPerkawinan:"Janda",       tujuan:"Singapura",  kategoriTujuan:"Luar Negeri"  }),
];

// ── Custom Tooltip ──
const ChartTooltip = ({ active, payload, label }: any) => {
  if (!active || !payload?.length) return null;
  const total = payload.reduce((s: number, p: any) => s + (p.value || 0), 0);
  return (
    <div className="bg-white border border-gray-100 rounded-xl shadow-lg px-4 py-3 text-sm">
      <p className="font-bold text-gray-800 mb-1.5 border-b pb-1.5">{label}</p>
      {payload.map((p: any) => (
        <div key={p.name} className="flex justify-between gap-4">
          <span style={{ color: p.color }} className="font-medium">{p.name}</span>
          <span className="font-bold text-gray-800">{p.value}</span>
        </div>
      ))}
      <div className="flex justify-between gap-4 border-t mt-1.5 pt-1.5">
        <span className="font-semibold text-gray-600">Total</span>
        <span className="font-bold text-gray-900">{total}</span>
      </div>
    </div>
  );
};

const DonutTooltip = ({ active, payload }: any) => {
  if (!active || !payload?.length) return null;
  const { name, value, payload: p } = payload[0];
  return (
    <div className="bg-white border border-gray-100 rounded-xl shadow-lg px-4 py-3 text-sm">
      <p className="font-bold mb-1" style={{ color: p.fill }}>{name}</p>
      <p className="text-gray-600">Jumlah: <span className="font-bold text-gray-900">{value}</span></p>
      <p className="text-gray-600">Persen: <span className="font-bold" style={{ color: p.fill }}>{p.persen}%</span></p>
    </div>
  );
};

export default function RekapAK1Page() {
  const [modeRekap, setModeRekap] = useState<"bulanan" | "triwulan">("bulanan");
  const [tahun, setTahun]         = useState(2026);
  const [bulan, setBulan]         = useState(2);
  const [triwulan, setTriwulan]   = useState(1);
  const [filterPendidikan, setFilterPendidikan] = useState("Semua");
  const [filterTujuan, setFilterTujuan]         = useState("Semua");

  const bulanAktif = useMemo(() => {
    if (modeRekap === "bulanan") return [bulan];
    return TRIWULAN_LIST.find((t) => t.value === triwulan)?.bulan ?? [1, 2, 3];
  }, [modeRekap, bulan, triwulan]);

  const periodeLabel = useMemo(() => {
    if (modeRekap === "bulanan") return `${BULAN_LABEL[bulan]} ${tahun}`;
    const tw = TRIWULAN_LIST.find((t) => t.value === triwulan);
    return `${tw?.label} ${tahun}`;
  }, [modeRekap, bulan, triwulan, tahun]);

  // ── Filter utama ──
  const filtered = useMemo(() =>
    rawData.filter((d) => d.tahun === tahun && bulanAktif.includes(d.bulan)),
    [tahun, bulanAktif]);

  const total   = filtered.length;
  const totalL  = filtered.filter((d) => d.jenisKelamin === "L").length;
  const totalP  = filtered.filter((d) => d.jenisKelamin === "P").length;

  // ── Line Chart: per bulan dalam periode ──
  const dataLine = useMemo(() =>
    bulanAktif.map((b) => {
      const rows = filtered.filter((d) => d.bulan === b);
      return {
        periode: BULAN_SHORT[b],
        "Laki-laki": rows.filter((d) => d.jenisKelamin === "L").length,
        "Perempuan":  rows.filter((d) => d.jenisKelamin === "P").length,
      };
    }), [filtered, bulanAktif]);

  // ── Bar Chart Pendidikan ──
  const dataPendidikan = useMemo(() => {
    const src = filterPendidikan === "Semua"
      ? filtered
      : filtered.filter((d) => d.pendidikan === filterPendidikan);
    return PENDIDIKAN_LIST
      .map((p) => {
        const rows = src.filter((d) => d.pendidikan === p);
        return {
          pendidikan: p,
          "Laki-laki": rows.filter((d) => d.jenisKelamin === "L").length,
          "Perempuan":  rows.filter((d) => d.jenisKelamin === "P").length,
          total: rows.length,
        };
      })
      .filter((d) => d.total > 0);
  }, [filtered, filterPendidikan]);

  // ── Donut Chart Status Perkawinan ──
  const dataKawin = useMemo(() => {
    return KAWIN_LIST.map((k) => {
      const value = filtered.filter((d) => d.statusPerkawinan === k).length;
      const persen = total > 0 ? parseFloat(((value / total) * 100).toFixed(1)) : 0;
      return { name: k, value, persen, fill: WARNA_KAWIN[k] };
    }).filter((d) => d.value > 0);
  }, [filtered, total]);

  // ── Bar Chart Tujuan ──
  const allTujuan = [...TUJUAN_DALAM, ...TUJUAN_LUAR];
  const dataTujuan = useMemo(() => {
    const src = filterTujuan === "Semua"
      ? filtered
      : filterTujuan === "Dalam Negeri"
      ? filtered.filter((d) => d.kategoriTujuan === "Dalam Negeri")
      : filtered.filter((d) => d.kategoriTujuan === "Luar Negeri");
    return allTujuan
      .map((t) => {
        const rows = src.filter((d) => d.tujuan === t);
        return {
          tujuan: t,
          "Laki-laki": rows.filter((d) => d.jenisKelamin === "L").length,
          "Perempuan":  rows.filter((d) => d.jenisKelamin === "P").length,
          total: rows.length,
          jenis: TUJUAN_LUAR.includes(t) ? "Luar Negeri" : "Dalam Negeri",
        };
      })
      .filter((d) => d.total > 0)
      .sort((a, b) => b.total - a.total);
  }, [filtered, filterTujuan]);

  const selectClass = "border border-gray-200 rounded-xl px-3 py-2 text-sm bg-white focus:outline-none focus:ring-2 focus:ring-[#1B4EF5]/30 focus:border-[#1B4EF5] cursor-pointer appearance-none pr-8";

  const Sel = ({ children }: { children: React.ReactNode }) => (
    <div className="relative">
      {children}
      <svg className="w-4 h-4 text-gray-400 absolute right-2 top-1/2 -translate-y-1/2 pointer-events-none"
        fill="none" stroke="currentColor" viewBox="0 0 24 24">
        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 9l-7 7-7-7" />
      </svg>
    </div>
  );

  // ── Card KPI ──
  const KPICard = ({ icon, label, value, sub, color = "blue" }: {
    icon: React.ReactNode; label: string; value: number | string; sub: string; color?: string;
  }) => {
    const bg: Record<string, string> = { blue: "bg-[#DDE5FE]", green: "bg-green-50", pink: "bg-pink-50", purple: "bg-purple-50" };
    const ic: Record<string, string> = { blue: "text-[#1B4EF5]", green: "text-green-600", pink: "text-pink-500", purple: "text-purple-600" };
    return (
      <div className="bg-white rounded-2xl p-5 border border-gray-100 shadow-sm flex items-center gap-4 relative overflow-hidden">
        <div className={`absolute right-0 top-0 w-20 h-20 rounded-full ${bg[color]} -translate-y-4 translate-x-4 opacity-50`} />
        <div className={`w-12 h-12 rounded-full ${bg[color]} flex items-center justify-center shrink-0 z-10 ${ic[color]}`}>
          {icon}
        </div>
        <div className="z-10">
          <p className="text-xs text-gray-500 font-medium">{label}</p>
          <p className="text-3xl font-black text-gray-900">{value}</p>
          <p className="text-xs text-gray-400">{sub}</p>
        </div>
      </div>
    );
  };

  return (
    <div className="space-y-6 pb-10">

      {/* ── Header ── */}
      <div>
        <p className="text-sm text-gray-500">Rekapitulasi Data</p>
        <h1 className="text-2xl font-black text-gray-900">AK-1 — Pencari Kerja</h1>
        <p className="text-sm text-gray-400 mt-1">{periodeLabel}</p>
      </div>

      {/* ══════════════════════════════
          FILTER GLOBAL
      ══════════════════════════════ */}
      <div className="bg-white rounded-2xl p-4 border border-gray-100 shadow-sm flex items-center gap-4 flex-wrap">

        {/* Mode Rekap */}
        <div className="flex items-center gap-1 bg-[#DDE5FE] rounded-xl p-1">
          {(["bulanan", "triwulan"] as const).map((m) => (
            <button
              key={m}
              onClick={() => setModeRekap(m)}
              className={`px-4 py-2 rounded-lg text-xs font-bold transition-all ${
                modeRekap === m ? "bg-[#1B4EF5] text-white shadow" : "text-[#1B4EF5] hover:bg-white/60"
              }`}
            >
              {m === "bulanan" ? "Bulanan" : "Triwulan"}
            </button>
          ))}
        </div>

        {/* Tahun */}
        <div className="flex items-center gap-2">
          <span className="text-xs text-gray-500 font-medium">Tahun</span>
          <Sel>
            <select value={tahun} onChange={(e) => setTahun(Number(e.target.value))} className={selectClass}>
              {TAHUN_LIST.map((t) => <option key={t} value={t}>{t}</option>)}
            </select>
          </Sel>
        </div>

        {/* Periode — Bulanan atau Triwulan */}
        <div className="flex items-center gap-2">
          <span className="text-xs text-gray-500 font-medium">Periode</span>
          {modeRekap === "bulanan" ? (
            <Sel>
              <select value={bulan} onChange={(e) => setBulan(Number(e.target.value))} className={selectClass}>
                {Array.from({ length: 12 }, (_, i) => i + 1).map((b) => (
                  <option key={b} value={b}>{BULAN_LABEL[b]}</option>
                ))}
              </select>
            </Sel>
          ) : (
            <Sel>
              <select value={triwulan} onChange={(e) => setTriwulan(Number(e.target.value))} className={selectClass}>
                {TRIWULAN_LIST.map((t) => (
                  <option key={t.value} value={t.value}>{t.label}</option>
                ))}
              </select>
            </Sel>
          )}
        </div>

        {/* Badge periode aktif */}
        <div className="ml-auto bg-[#DDE5FE] rounded-xl px-4 py-2 text-xs font-bold text-[#1B4EF5]">
          {periodeLabel}
        </div>
      </div>

      {/* ══════════════════════════════
          KPI CARDS
      ══════════════════════════════ */}
      <div className="grid grid-cols-4 gap-4">
        <KPICard
          color="blue" value={total} label="Total Pencari Kerja" sub="orang terdaftar"
          icon={<svg className="w-6 h-6" fill="currentColor" viewBox="0 0 24 24"><path d="M16 11c1.66 0 2.99-1.34 2.99-3S17.66 5 16 5c-1.66 0-3 1.34-3 3s1.34 3 3 3zm-8 0c1.66 0 2.99-1.34 2.99-3S9.66 5 8 5C6.34 5 5 6.34 5 8s1.34 3 3 3zm0 2c-2.33 0-7 1.17-7 3.5V19h14v-2.5c0-2.33-4.67-3.5-7-3.5zm8 0c-.29 0-.62.02-.97.05 1.16.84 1.97 1.97 1.97 3.45V19h6v-2.5c0-2.33-4.67-3.5-7-3.5z"/></svg>}
        />
        <KPICard
          color="blue" value={totalL} label="Total Laki-laki" sub={`${total > 0 ? ((totalL/total)*100).toFixed(1) : 0}% dari total`}
          icon={<svg className="w-6 h-6" fill="currentColor" viewBox="0 0 24 24"><path d="M12 12c2.7 0 4.8-2.1 4.8-4.8S14.7 2.4 12 2.4 7.2 4.5 7.2 7.2 9.3 12 12 12zm0 2.4c-3.2 0-9.6 1.6-9.6 4.8v2.4h19.2v-2.4c0-3.2-6.4-4.8-9.6-4.8z"/></svg>}
        />
        <KPICard
          color="pink" value={totalP} label="Total Perempuan" sub={`${total > 0 ? ((totalP/total)*100).toFixed(1) : 0}% dari total`}
          icon={<svg className="w-6 h-6" fill="currentColor" viewBox="0 0 24 24"><path d="M12 12c2.7 0 4.8-2.1 4.8-4.8S14.7 2.4 12 2.4 7.2 4.5 7.2 7.2 9.3 12 12 12zm0 2.4c-3.2 0-9.6 1.6-9.6 4.8v2.4h19.2v-2.4c0-3.2-6.4-4.8-9.6-4.8z"/></svg>}
        />
        <KPICard
          color="purple" value={periodeLabel} label="Periode Aktif" sub={modeRekap === "bulanan" ? "Mode Bulanan" : "Mode Triwulan"}
          icon={<svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z"/></svg>}
        />
      </div>

      {/* ══════════════════════════════
          GRAFIK 1 — Line Chart Perkembangan
      ══════════════════════════════ */}
      <div className="bg-white rounded-2xl p-6 border border-gray-100 shadow-sm">
        <div className="mb-4">
          <h2 className="font-bold text-gray-900 text-base">Perkembangan Pencari Kerja</h2>
          <p className="text-xs text-gray-400 mt-0.5">
            Tren jumlah pencari kerja laki-laki dan perempuan — {periodeLabel}
          </p>
        </div>
        {total === 0 ? (
          <div className="flex items-center justify-center h-48 text-gray-400 text-sm">Tidak ada data</div>
        ) : (
          <ResponsiveContainer width="100%" height={260}>
            <LineChart data={dataLine} margin={{ left: 8, right: 24, top: 8, bottom: 4 }}>
              <CartesianGrid strokeDasharray="3 3" stroke="#f0f0f0" />
              <XAxis dataKey="periode" tick={{ fontSize: 12, fill: "#6b7280" }} />
              <YAxis tick={{ fontSize: 11, fill: "#9ca3af" }} allowDecimals={false} />
              <Tooltip content={<ChartTooltip />} />
              <Legend iconType="circle" iconSize={8} wrapperStyle={{ fontSize: 12 }} />
              <Line type="monotone" dataKey="Laki-laki" stroke={BIRU} strokeWidth={2.5}
                dot={{ r: 5, fill: BIRU }} activeDot={{ r: 7 }} />
              <Line type="monotone" dataKey="Perempuan" stroke={BIRU_MUDA} strokeWidth={2.5}
                dot={{ r: 5, fill: BIRU_MUDA }} activeDot={{ r: 7 }} />
            </LineChart>
          </ResponsiveContainer>
        )}

        {/* Tabel ringkasan line */}
        {total > 0 && (
          <div className="mt-4 overflow-x-auto">
            <table className="w-full text-xs">
              <thead>
                <tr className="bg-[#DDE5FE] text-gray-700">
                  <th className="text-left px-3 py-2 font-semibold rounded-tl-xl">Periode</th>
                  <th className="text-center px-3 py-2 font-semibold text-[#1B4EF5]">Laki-laki</th>
                  <th className="text-center px-3 py-2 font-semibold text-[#93AEFB]">Perempuan</th>
                  <th className="text-center px-3 py-2 font-semibold rounded-tr-xl">Total</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-gray-100">
                {dataLine.map((row) => (
                  <tr key={row.periode} className="hover:bg-gray-50">
                    <td className="px-3 py-2 font-medium text-gray-700">{row.periode}</td>
                    <td className="px-3 py-2 text-center font-bold text-[#1B4EF5]">{row["Laki-laki"]}</td>
                    <td className="px-3 py-2 text-center font-bold text-[#93AEFB]">{row["Perempuan"]}</td>
                    <td className="px-3 py-2 text-center font-black text-gray-900">{row["Laki-laki"] + row["Perempuan"]}</td>
                  </tr>
                ))}
              </tbody>
              <tfoot>
                <tr className="bg-[#DDE5FE] font-bold">
                  <td className="px-3 py-2 text-[#1B4EF5] rounded-bl-xl">Total</td>
                  <td className="px-3 py-2 text-center text-[#1B4EF5]">{totalL}</td>
                  <td className="px-3 py-2 text-center text-[#93AEFB]">{totalP}</td>
                  <td className="px-3 py-2 text-center text-gray-900 rounded-br-xl">{total}</td>
                </tr>
              </tfoot>
            </table>
          </div>
        )}
      </div>

      {/* ══════════════════════════════
          GRAFIK 2 & 3 — Pendidikan + Status Perkawinan
      ══════════════════════════════ */}
      <div className="grid grid-cols-2 gap-4">

        {/* Pendidikan */}
        <div className="bg-white rounded-2xl p-6 border border-gray-100 shadow-sm">
          <div className="flex items-start justify-between mb-4 flex-wrap gap-3">
            <div>
              <h2 className="font-bold text-gray-900 text-base">Rekap Pendidikan</h2>
              <p className="text-xs text-gray-400 mt-0.5">Jumlah pencari kerja per jenjang pendidikan</p>
            </div>
            <div className="relative">
              <select
                value={filterPendidikan}
                onChange={(e) => setFilterPendidikan(e.target.value)}
                className={`${selectClass} text-xs`}
              >
                <option value="Semua">Semua Pendidikan</option>
                {PENDIDIKAN_LIST.map((p) => <option key={p} value={p}>{p}</option>)}
              </select>
              <svg className="w-4 h-4 text-gray-400 absolute right-2 top-1/2 -translate-y-1/2 pointer-events-none" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 9l-7 7-7-7" />
              </svg>
            </div>
          </div>

          {dataPendidikan.length === 0 ? (
            <div className="flex items-center justify-center h-48 text-gray-400 text-sm">Tidak ada data</div>
          ) : (
            <>
              <ResponsiveContainer width="100%" height={250}>
                <BarChart data={dataPendidikan} layout="vertical" margin={{ left: 8, right: 48, top: 4, bottom: 4 }}>
                  <CartesianGrid strokeDasharray="3 3" horizontal={false} stroke="#f0f0f0" />
                  <XAxis type="number" tick={{ fontSize: 11, fill: "#9ca3af" }} allowDecimals={false} />
                  <YAxis type="category" dataKey="pendidikan" tick={{ fontSize: 11, fill: "#6b7280" }} width={100} />
                  <Tooltip content={<ChartTooltip />} />
                  <Legend iconType="circle" iconSize={8} wrapperStyle={{ fontSize: 12 }} />
                  <Bar dataKey="Laki-laki" fill={BIRU} radius={[0, 4, 4, 0]} barSize={10}>
                    <LabelList dataKey="Laki-laki" position="right" style={{ fontSize: 10, fill: "#374151", fontWeight: 600 }}
                      formatter={((v: number) => v > 0 ? v : "") as any} />
                  </Bar>
                  <Bar dataKey="Perempuan" fill={BIRU_MUDA} radius={[0, 4, 4, 0]} barSize={10}>
                    <LabelList dataKey="Perempuan" position="right" style={{ fontSize: 10, fill: "#374151", fontWeight: 600 }}
                      formatter={((v: number) => v > 0 ? v : "") as any} />
                  </Bar>
                </BarChart>
              </ResponsiveContainer>

              <table className="w-full text-xs mt-4">
                <thead>
                  <tr className="bg-[#DDE5FE] text-gray-700">
                    <th className="text-left px-3 py-2 font-semibold rounded-tl-xl">Pendidikan</th>
                    <th className="text-center px-3 py-2 font-semibold">L</th>
                    <th className="text-center px-3 py-2 font-semibold">P</th>
                    <th className="text-center px-3 py-2 font-semibold rounded-tr-xl">Total</th>
                  </tr>
                </thead>
                <tbody className="divide-y divide-gray-100">
                  {dataPendidikan.map((row) => (
                    <tr key={row.pendidikan} className="hover:bg-gray-50">
                      <td className="px-3 py-2 font-medium text-gray-700">{row.pendidikan}</td>
                      <td className="px-3 py-2 text-center font-bold text-[#1B4EF5]">{row["Laki-laki"]}</td>
                      <td className="px-3 py-2 text-center font-bold text-[#93AEFB]">{row["Perempuan"]}</td>
                      <td className="px-3 py-2 text-center font-black text-gray-900">{row.total}</td>
                    </tr>
                  ))}
                </tbody>
                <tfoot>
                  <tr className="bg-[#DDE5FE] font-bold">
                    <td className="px-3 py-2 text-[#1B4EF5] rounded-bl-xl">Total</td>
                    <td className="px-3 py-2 text-center text-[#1B4EF5]">{dataPendidikan.reduce((s, d) => s + d["Laki-laki"], 0)}</td>
                    <td className="px-3 py-2 text-center text-[#93AEFB]">{dataPendidikan.reduce((s, d) => s + d["Perempuan"], 0)}</td>
                    <td className="px-3 py-2 text-center text-gray-900 rounded-br-xl">{dataPendidikan.reduce((s, d) => s + d.total, 0)}</td>
                  </tr>
                </tfoot>
              </table>
            </>
          )}
        </div>

        {/* Status Perkawinan — Donut */}
        <div className="bg-white rounded-2xl p-6 border border-gray-100 shadow-sm">
          <div className="mb-4">
            <h2 className="font-bold text-gray-900 text-base">Rekap Status Perkawinan</h2>
            <p className="text-xs text-gray-400 mt-0.5">Distribusi pencari kerja berdasarkan status perkawinan</p>
          </div>

          {total === 0 ? (
            <div className="flex items-center justify-center h-48 text-gray-400 text-sm">Tidak ada data</div>
          ) : (
            <>
              <div className="flex items-center gap-4">
                {/* Donut */}
                <div className="relative flex-shrink-0">
                  <ResponsiveContainer width={200} height={200}>
                    <PieChart>
                      <Pie
                        data={dataKawin}
                        cx="50%" cy="50%"
                        innerRadius={60} outerRadius={90}
                        paddingAngle={3}
                        dataKey="value"
                        startAngle={90} endAngle={-270}
                      >
                        {dataKawin.map((entry, i) => (
                          <Cell key={i} fill={entry.fill} />
                        ))}
                      </Pie>
                      <Tooltip content={<DonutTooltip />} />
                    </PieChart>
                  </ResponsiveContainer>
                  {/* Label tengah */}
                  <div className="absolute inset-0 flex flex-col items-center justify-center pointer-events-none">
                    <p className="text-2xl font-black text-gray-900">{total}</p>
                    <p className="text-xs text-gray-400 text-center leading-tight">Total<br/>Pencaker</p>
                  </div>
                </div>

                {/* Legend */}
                <div className="flex-1 space-y-3">
                  {dataKawin.map((item) => (
                    <div key={item.name} className="flex items-center justify-between gap-2">
                      <div className="flex items-center gap-2">
                        <div className="w-3 h-3 rounded-full shrink-0" style={{ background: item.fill }} />
                        <span className="text-sm text-gray-700 font-medium">{item.name}</span>
                      </div>
                      <div className="text-right">
                        <span className="text-sm font-black text-gray-900">{item.value}</span>
                        <span className="text-xs text-gray-400 ml-1">({item.persen}%)</span>
                      </div>
                    </div>
                  ))}
                  {/* Total baris */}
                  <div className="border-t pt-2 flex items-center justify-between">
                    <span className="text-sm font-bold text-[#1B4EF5]">Total</span>
                    <span className="text-sm font-black text-gray-900">{total} (100%)</span>
                  </div>
                </div>
              </div>

              {/* Tabel */}
              <table className="w-full text-xs mt-4">
                <thead>
                  <tr className="bg-[#DDE5FE] text-gray-700">
                    <th className="text-left px-3 py-2 font-semibold rounded-tl-xl">Status</th>
                    <th className="text-center px-3 py-2 font-semibold">Jumlah</th>
                    <th className="text-center px-3 py-2 font-semibold rounded-tr-xl">%</th>
                  </tr>
                </thead>
                <tbody className="divide-y divide-gray-100">
                  {dataKawin.map((row) => (
                    <tr key={row.name} className="hover:bg-gray-50">
                      <td className="px-3 py-2">
                        <div className="flex items-center gap-2">
                          <div className="w-2.5 h-2.5 rounded-full" style={{ background: row.fill }} />
                          <span className="font-medium text-gray-700">{row.name}</span>
                        </div>
                      </td>
                      <td className="px-3 py-2 text-center font-black text-gray-900">{row.value}</td>
                      <td className="px-3 py-2 text-center">
                        <span className="px-2 py-0.5 rounded-full text-xs font-bold"
                          style={{ background: `${row.fill}20`, color: row.fill }}>
                          {row.persen}%
                        </span>
                      </td>
                    </tr>
                  ))}
                </tbody>
                <tfoot>
                  <tr className="bg-[#DDE5FE] font-bold">
                    <td className="px-3 py-2 text-[#1B4EF5] rounded-bl-xl">Total</td>
                    <td className="px-3 py-2 text-center text-gray-900">{total}</td>
                    <td className="px-3 py-2 text-center text-gray-900 rounded-br-xl">100%</td>
                  </tr>
                </tfoot>
              </table>
            </>
          )}
        </div>
      </div>

      {/* ══════════════════════════════
          GRAFIK 4 — Rekap Tujuan
      ══════════════════════════════ */}
      <div className="bg-white rounded-2xl p-6 border border-gray-100 shadow-sm">
        <div className="flex items-start justify-between mb-4 flex-wrap gap-3">
          <div>
            <h2 className="font-bold text-gray-900 text-base">Rekap Tempat / Negara Tujuan</h2>
            <p className="text-xs text-gray-400 mt-0.5">Jumlah pencari kerja berdasarkan tujuan penempatan</p>
          </div>
          <div className="flex items-center gap-2">
            <div className="relative">
              <select value={filterTujuan} onChange={(e) => setFilterTujuan(e.target.value)} className={`${selectClass} text-xs`}>
                <option value="Semua">Semua Tujuan</option>
                <option value="Dalam Negeri">Dalam Negeri</option>
                <option value="Luar Negeri">Luar Negeri</option>
              </select>
              <svg className="w-4 h-4 text-gray-400 absolute right-2 top-1/2 -translate-y-1/2 pointer-events-none" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 9l-7 7-7-7" />
              </svg>
            </div>
            <span className="text-xs bg-[#DDE5FE] text-[#1B4EF5] font-bold px-3 py-1.5 rounded-xl">
              {dataTujuan.length} tujuan ditemukan
            </span>
          </div>
        </div>

        {dataTujuan.length === 0 ? (
          <div className="flex items-center justify-center h-40 text-gray-400 text-sm">Tidak ada data</div>
        ) : (
          <div className="grid grid-cols-2 gap-6">
            <ResponsiveContainer width="100%" height={Math.max(200, dataTujuan.length * 36)}>
              <BarChart data={dataTujuan} layout="vertical" margin={{ left: 8, right: 56, top: 4, bottom: 4 }}>
                <CartesianGrid strokeDasharray="3 3" horizontal={false} stroke="#f0f0f0" />
                <XAxis type="number" tick={{ fontSize: 11, fill: "#9ca3af" }} allowDecimals={false} />
                <YAxis type="category" dataKey="tujuan" tick={{ fontSize: 11, fill: "#6b7280" }} width={110} />
                <Tooltip content={<ChartTooltip />} />
                <Legend iconType="circle" iconSize={8} wrapperStyle={{ fontSize: 12 }} />
                <Bar dataKey="Laki-laki" fill={BIRU} radius={[0, 4, 4, 0]} barSize={10}>
                  <LabelList dataKey="Laki-laki" position="right" style={{ fontSize: 10, fill: "#374151", fontWeight: 600 }}
                    formatter={((v: number) => v > 0 ? v : "") as any} />
                </Bar>
                <Bar dataKey="Perempuan" fill={BIRU_MUDA} radius={[0, 4, 4, 0]} barSize={10}>
                  <LabelList dataKey="Perempuan" position="right" style={{ fontSize: 10, fill: "#374151", fontWeight: 600 }}
                    formatter={((v: number) => v > 0 ? v : "") as any} />
                </Bar>
              </BarChart>
            </ResponsiveContainer>

            {/* Tabel tujuan */}
            <div className="overflow-y-auto max-h-80">
              <table className="w-full text-xs">
                <thead className="sticky top-0">
                  <tr className="bg-[#DDE5FE] text-gray-700">
                    <th className="text-left px-3 py-2 font-semibold rounded-tl-xl">Tujuan</th>
                    <th className="text-center px-3 py-2 font-semibold">Jenis</th>
                    <th className="text-center px-3 py-2 font-semibold">L</th>
                    <th className="text-center px-3 py-2 font-semibold">P</th>
                    <th className="text-center px-3 py-2 font-semibold rounded-tr-xl">Total</th>
                  </tr>
                </thead>
                <tbody className="divide-y divide-gray-100">
                  {dataTujuan.map((row) => (
                    <tr key={row.tujuan} className="hover:bg-gray-50">
                      <td className="px-3 py-2 font-medium text-gray-700">{row.tujuan}</td>
                      <td className="px-3 py-2 text-center">
                        <span className={`px-2 py-0.5 rounded-full text-xs font-bold ${
                          row.jenis === "Luar Negeri"
                            ? "bg-blue-100 text-blue-600"
                            : "bg-green-100 text-green-600"
                        }`}>
                          {row.jenis === "Luar Negeri" ? "" : ""} {row.jenis}
                        </span>
                      </td>
                      <td className="px-3 py-2 text-center font-bold text-[#1B4EF5]">{row["Laki-laki"]}</td>
                      <td className="px-3 py-2 text-center font-bold text-[#93AEFB]">{row["Perempuan"]}</td>
                      <td className="px-3 py-2 text-center font-black text-gray-900">{row.total}</td>
                    </tr>
                  ))}
                </tbody>
                <tfoot>
                  <tr className="bg-[#DDE5FE] font-bold">
                    <td colSpan={2} className="px-3 py-2 text-[#1B4EF5] rounded-bl-xl">Total</td>
                    <td className="px-3 py-2 text-center text-[#1B4EF5]">{dataTujuan.reduce((s, d) => s + d["Laki-laki"], 0)}</td>
                    <td className="px-3 py-2 text-center text-[#93AEFB]">{dataTujuan.reduce((s, d) => s + d["Perempuan"], 0)}</td>
                    <td className="px-3 py-2 text-center text-gray-900 rounded-br-xl">{dataTujuan.reduce((s, d) => s + d.total, 0)}</td>
                  </tr>
                </tfoot>
              </table>
            </div>
          </div>
        )}
      </div>

    </div>
  );
}