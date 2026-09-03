"use client";

import Link from "next/link";
import { usePathname, useRouter } from "next/navigation";
import { useState } from "react";

const menuItems = [
  {
    label: "Dashboard",
    href: "/dashboard",
    icon: (
      <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24">
        <path d="M0 0h24v24H0z" fill="none" />
        <path fill="currentColor" d="M14.305 9q-.345 0-.575-.23t-.23-.587V4.817q0-.357.234-.587t.58-.23h4.88q.347 0 .576.23t.23.587v3.366q0 .358-.234.587q-.234.23-.58.23zm-9.5 3q-.345 0-.575-.23T4 11.2V4.8q0-.34.234-.57t.58-.23h4.88q.347 0 .576.23t.23.57v6.4q0 .34-.234.57t-.58.23zm9.5 8q-.345 0-.575-.23t-.23-.57v-6.4q0-.34.234-.57t.58-.23h4.88q.347 0 .576.23t.23.57v6.4q0 .34-.234.57t-.58.23zm-9.5 0q-.345 0-.575-.23T4 19.183v-3.366q0-.357.234-.587t.58-.23h4.88q.347 0 .576.23t.23.587v3.366q0 .358-.234.587q-.234.23-.58.23z" />
      </svg>
    ),
  },
  {
    label: "CPMI",
    href: "/cpmi",
    icon: (
      <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24">
        <path d="M0 0h24v24H0z" fill="none" />
        <path fill="currentColor" d="M12 3c2.21 0 4 1.79 4 4s-1.79 4-4 4s-4-1.79-4-4s1.79-4 4-4m4 10.54c0 1.06-.28 3.53-2.19 6.29L13 15l.94-1.88c-.62-.07-1.27-.12-1.94-.12s-1.32.05-1.94.12L11 15l-.81 4.83C8.28 17.07 8 14.6 8 13.54c-2.39.7-4 1.96-4 3.46v4h16v-4c0-1.5-1.6-2.76-4-3.46" />
      </svg>
    ),
  },
  {
    label: "AK-1",
    href: "/ak1",
    icon: (
      <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24">
        <path d="M0 0h24v24H0z" fill="none" />
        <path fill="currentColor" d="M20 4H4c-1.1 0-2 .9-2 2v12c0 1.1.9 2 2 2h16c1.1 0 2-.9 2-2V6c0-1.1-.9-2-2-2M9 8c1.15 0 2 .85 2 2s-.85 2-2 2s-2-.85-2-2s.85-2 2-2m-3 8c0-1.66 1.34-3 3-3s3 1.34 3 3zm12-1h-4v-2h4zm0-4h-5V9h5z" />
      </svg>
    ),
  },
  {
    label: "PMI Non Prosedur",
    href: "/pmi-non-prosedur",
    icon: (
      <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24">
        <path d="M0 0h24v24H0z" fill="none" />
        <path fill="currentColor" d="M10 4a4 4 0 0 1 4 4a4 4 0 0 1-4 4a4 4 0 0 1-4-4a4 4 0 0 1 4-4m0 10c4.42 0 8 1.79 8 4v2H2v-2c0-2.21 3.58-4 8-4m10-2V7h2v6h-2m0 4v-2h2v2z" />
      </svg>
    ),
  },
  {
    label: "Disabilitas",
    href: "/disabilitas",
    icon: (
      <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24">
        <path d="M0 0h24v24H0z" fill="none" />
        <path fill="currentColor" d="M8 6c0-2.21 1.79-4 4-4s4 1.79 4 4s-1.79 4-4 4s-4-1.79-4-4m9 16h1c1.1 0 2-.9 2-2v-4.78c0-1.12-.61-2.15-1.61-2.66c-.43-.22-.89-.43-1.39-.62zm-4.66-5L15 11.33c-.93-.21-1.93-.33-3-.33c-2.53 0-4.71.7-6.39 1.56A2.97 2.97 0 0 0 4 15.22V22h2.34c-.22-.45-.34-.96-.34-1.5C6 18.57 7.57 17 9.5 17zM10 22l1.41-3H9.5c-.83 0-1.5.67-1.5 1.5S8.67 22 9.5 22z" />
      </svg>
    ),
    submenu: [
      { label: "Perusahaan", href: "/disabilitas/perusahaan" },
      { label: "Lansia", href: "/disabilitas/lansia" },
    ],
  },
];

export default function Sidebar() {
  const pathname = usePathname();
  const router = useRouter();
  const [collapsed, setCollapsed] = useState(false);
  const [openSubmenu, setOpenSubmenu] = useState<string | null>(
    // Buka submenu Disabilitas otomatis jika sedang di halaman disabilitas
    pathname.startsWith("/disabilitas") ? "Disabilitas" : null
  );

  const handleLogout = () => {
    localStorage.removeItem("token");
    router.push("/login");
  };

  const toggleSubmenu = (label: string) => {
    setOpenSubmenu(openSubmenu === label ? null : label);
  };

  return (
    <aside
      className={`${
        collapsed ? "w-[72px]" : "w-64"
      } min-h-screen bg-[#1B4EF5] flex flex-col shrink-0 transition-all duration-300 ease-in-out`}
    >
      {/* Tombol Toggle */}
      <div className={`flex ${collapsed ? "justify-center" : "justify-end"} px-4 pt-5 pb-3`}>
        <button
          onClick={() => setCollapsed(!collapsed)}
          className="text-white/80 hover:text-white hover:bg-white/10 p-2 rounded-xl transition-all duration-200"
          title={collapsed ? "Buka sidebar" : "Tutup sidebar"}
        >
          <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2}
              d="M4 6h16M4 12h16M4 18h16" />
          </svg>
        </button>
      </div>

      {/* Divider */}
      <div className="mx-4 border-t border-white/10 mb-3" />

      {/* Menu Items */}
      <nav className="flex-1 px-3 space-y-1">
        {menuItems.map((item) => {
          const isActive = pathname === item.href || pathname.startsWith(item.href + "/");
          const hasSubmenu = !!item.submenu;
          const isSubmenuOpen = openSubmenu === item.label;

          return (
            <div key={item.href}>
              {/* Menu utama */}
              {hasSubmenu ? (
                // Item dengan submenu — pakai button bukan Link
                <button
                  onClick={() => {
                    if (collapsed) {
                      setCollapsed(false);
                      setOpenSubmenu(item.label);
                    } else {
                      toggleSubmenu(item.label);
                    }
                  }}
                  title={collapsed ? item.label : undefined}
                  className={`flex items-center gap-3 px-3 py-3 rounded-xl transition-all duration-200 group w-full ${
                    collapsed ? "justify-center" : ""
                  } ${
                    isActive
                      ? "bg-white text-[#1B4EF5] font-bold shadow-md"
                      : "text-white/80 hover:bg-white/10 hover:text-white"
                  }`}
                >
                  <span className={isActive ? "text-[#1B4EF5]" : "text-white/80 group-hover:text-white"}>
                    {item.icon}
                  </span>

                  {!collapsed && (
                    <>
                      <span className="text-sm font-medium truncate flex-1 text-left">
                        {item.label}
                      </span>
                      {/* Chevron rotate saat terbuka */}
                      <svg
                        className={`w-4 h-4 shrink-0 transition-transform duration-200 ${
                          isSubmenuOpen ? "rotate-180" : ""
                        }`}
                        fill="none"
                        stroke="currentColor"
                        viewBox="0 0 24 24"
                      >
                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2}
                          d="M19 9l-7 7-7-7" />
                      </svg>
                    </>
                  )}
                </button>
              ) : (
                // Item biasa — pakai Link
                <Link
                  href={item.href}
                  title={collapsed ? item.label : undefined}
                  className={`flex items-center gap-3 px-3 py-3 rounded-xl transition-all duration-200 group ${
                    collapsed ? "justify-center" : ""
                  } ${
                    isActive
                      ? "bg-white text-[#1B4EF5] font-bold shadow-md"
                      : "text-white/80 hover:bg-white/10 hover:text-white"
                  }`}
                >
                  <span className={isActive ? "text-[#1B4EF5]" : "text-white/80 group-hover:text-white"}>
                    {item.icon}
                  </span>
                  {!collapsed && (
                    <span className="text-sm font-medium truncate">{item.label}</span>
                  )}
                </Link>
              )}

              {/* Submenu — tampil saat terbuka & sidebar tidak collapsed */}
              {hasSubmenu && isSubmenuOpen && !collapsed && (
                <div className="mt-1 ml-4 pl-4 border-l border-white/20 space-y-1">
                  {item.submenu!.map((sub) => {
                    const isSubActive = pathname === sub.href;
                    return (
                      <Link
                        key={sub.href}
                        href={sub.href}
                        className={`flex items-center px-3 py-2 rounded-xl text-sm transition-all duration-200 ${
                          isSubActive
                            ? "bg-white text-[#1B4EF5] font-bold shadow-md"
                            : "text-white/80 hover:bg-white/10 hover:text-white"
                        }`}
                      >
                        {sub.label}
                      </Link>
                    );
                  })}
                </div>
              )}
            </div>
          );
        })}
      </nav>

      {/* Logout */}
      <div className="px-3 py-5 border-t border-white/10">
        <button
          onClick={handleLogout}
          title={collapsed ? "Logout" : undefined}
          className={`flex items-center gap-3 px-3 py-3 rounded-xl text-white/80 hover:bg-white/10 hover:text-white transition-all duration-200 w-full ${
            collapsed ? "justify-center" : ""
          }`}
        >
          <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24">
            <path d="M0 0h24v24H0z" fill="none" />
            <path
              fill="currentColor"
              fillRule="evenodd"
              clipRule="evenodd"
              d="M6 2a3 3 0 0 0-3 3v14a3 3 0 0 0 3 3h6a3 3 0 0 0 3-3V5a3 3 0 0 0-3-3zm10.293 5.293a1 1 0 0 1 1.414 0l4 4a1 1 0 0 1 0 1.414l-4 4a1 1 0 0 1-1.414-1.414L18.586 13H10a1 1 0 1 1 0-2h8.586l-2.293-2.293a1 1 0 0 1 0-1.414"
            />
          </svg>
          {!collapsed && <span className="text-sm font-medium">Logout</span>}
        </button>
      </div>
    </aside>
  );
}