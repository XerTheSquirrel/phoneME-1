/*
 *
 * Copyright  1990-2007 Sun Microsystems, Inc. All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License version
 * 2 only, as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License version 2 for more details (a copy is
 * included at /legal/license.txt).
 *
 * You should have received a copy of the GNU General Public License
 * version 2 along with this work; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA
 *
 * Please contact Sun Microsystems, Inc., 4150 Network Circle, Santa
 * Clara, CA 95054 or visit www.sun.com if you need additional
 * information or have any questions.
 *
 */

#include "splash.h"
#include "aygshell.h"

/* Forward declarations of functions included in this code module: */
ATOM            MyRegisterClass(HINSTANCE, LPTSTR);
BOOL            InitInstance(HINSTANCE, int);
BOOL            myInit();
LRESULT CALLBACK    WndProc(HWND, UINT, WPARAM, LPARAM);

const int splashWidth = 240, splashHeight=320;
int splashX = 0, splashY = 0;
HBITMAP g_hB;
HWND g_hWnd;
HDC g_dcBitmap = NULL;
/* If true than call exit after destroying the splash window */
BOOL g_bExitAfterSplash = FALSE;

DWORD WINAPI MessageLoop( LPVOID lpParam )
{

    MSG msg;
    /* Perform application initialization: */
    if (!myInit())
    {
        return 0;
    }
    /* Main message loop: */
    while (GetMessage(&msg, NULL, 0, 0))
    {
        if (!TranslateAccelerator(msg.hwnd, NULL, &msg))
        {
            TranslateMessage(&msg);
            DispatchMessage(&msg);
        }
    }
    if (g_bExitAfterSplash) {
        exit((int) msg.wParam);
    }
    return (int) msg.wParam;
}

void showSplash()
{
    CreateThread(NULL, 0, MessageLoop, NULL, 0, NULL);
}

ATOM MyRegisterClass(HINSTANCE hInstance, LPTSTR szWindowClass)
{
    WNDCLASS wc;

    wc.style         = CS_HREDRAW | CS_VREDRAW;
    wc.lpfnWndProc   = WndProc;
    wc.cbClsExtra    = 0;
    wc.cbWndExtra    = 0;
    wc.hInstance     = hInstance;
    wc.hIcon         = NULL;
    wc.hCursor       = 0;
    wc.hbrBackground = (HBRUSH) GetStockObject(WHITE_BRUSH);
    wc.lpszMenuName  = 0;
    wc.lpszClassName = szWindowClass;

    if (!RegisterClass(&wc)) {
        fprintf(stderr, "RegisterClass failed. err=0x%x\n", GetLastError());
        return FALSE;
    }
    return TRUE;
}

BOOL myInit()
{
    HWND hDesktop;
    RECT rect;
    HWND hWnd;
    int width;
    int height;
    HINSTANCE hInst; /* current instance */

    /* Store instance handle in our global variable */
    hInst = GetModuleHandle(NULL);

    if (!MyRegisterClass(hInst, _T("JavaSplash"))) {
        return FALSE;
    }

    hDesktop = GetDesktopWindow();
    GetClientRect(hDesktop, &rect);
    width = rect.right-rect.left;
    height = rect.bottom-rect.top;
    splashX = width/2 - splashWidth/2;
    splashY = height/2 - splashHeight/2;

    hWnd = CreateWindow(_T("JavaSplash"), _T("Java"), WS_VISIBLE,
        CW_USEDEFAULT, CW_USEDEFAULT, CW_USEDEFAULT, CW_USEDEFAULT,
        NULL, NULL, hInst, NULL);
    g_hWnd = hWnd;
    g_hB = LoadBitmap(hInst, (LPCTSTR)IDB_SPLASH);
    if (g_hB == NULL) {
        fprintf(stderr,"LoadBitmap failed\n");
        return FALSE;
    }

    SHFullScreen(hWnd, SHFS_HIDETASKBAR | SHFS_HIDESTARTICON | SHFS_HIDESIPBUTTON);
    ShowWindow(hWnd, SW_SHOW);
    UpdateWindow(hWnd);

    return TRUE;
}

void OnPaint(HWND hWnd, HDC hdc)
{
    RECT rect, rect1;
    int width, height;
    SystemParametersInfo(SPI_GETWORKAREA, 0, &rect, FALSE);
    width = rect.right-rect.left;
    height = rect.bottom-rect.top;
    if (!g_dcBitmap) {
        g_dcBitmap = CreateCompatibleDC(hdc);
        SelectObject(g_dcBitmap, (HGDIOBJ)g_hB);
    }
    BitBlt(hdc, splashX, splashY, splashWidth, splashHeight, g_dcBitmap,
        0, 0, SRCCOPY);
}

LRESULT CALLBACK WndProc(HWND hWnd, UINT message, WPARAM wParam, LPARAM lParam)
{
    PAINTSTRUCT ps;
    HDC hdc;
    switch (message)
    {
    case WM_CLOSE:
        g_bExitAfterSplash = TRUE;
        PostQuitMessage(0);
        break;
    case WM_SETFOCUS:
        SHFullScreen(hWnd, SHFS_HIDETASKBAR | SHFS_HIDESTARTICON | SHFS_HIDESIPBUTTON);
        break;
    case WM_PAINT:
        hdc = BeginPaint(hWnd, &ps);
        OnPaint(hWnd, hdc);
        EndPaint(hWnd, &ps);
        break;
    case WM_DESTROY:
        PostQuitMessage(0);
        break;

    default:
        return DefWindowProc(hWnd, message, wParam, lParam);
    }
    return 0;
}

void hideSplash()
{
    if (g_dcBitmap != NULL)
        DeleteDC(g_dcBitmap);
    g_dcBitmap = NULL;
    if (g_hWnd != NULL)
        DestroyWindow(g_hWnd);
}
