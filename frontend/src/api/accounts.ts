export type Account = {
    id: number
    userName: string
    type: string
    balance: number | string
    createdAt: string
}

export type Transaction = {
    id: number
    type: string
    amount: number | string
    timestamp: string
}

export type AmountRequest = {
    amount: number
}

const API_URL = import.meta.env.VITE_API_URL
const apiRoot = `${API_URL}/api/accounts`

async function request<T>(url: string, options?: RequestInit): Promise<T> {
    const token = localStorage.getItem('token')
    const response = await fetch(url, {
        ...options,
        headers: {
            'Content-Type': 'application/json',
            ...(token ? { Authorization: `Bearer ${token}` } : {}),
            ...options?.headers,
        },
    })

    if (!response.ok) {
        let message = response.statusText || `Request failed with status ${response.status}`
        try {
            const body = await response.json() as { message?: string; error?: string }
            message = body.message || body.error || message
        } catch {
            // Fall back to the HTTP status when the server does not return JSON.
        }
        throw new Error(message)
    }

    return response.json() as Promise<T>
}

export function getAccount(accountId: number): Promise<Account> {
    return request<Account>(`${apiRoot}/${accountId}`)
}

export function deposit(accountId: number, amount: number): Promise<Account> {
    return request<Account>(`${apiRoot}/${accountId}/deposit`, {
        method: 'POST',
        body: JSON.stringify({ amount } satisfies AmountRequest),
    })
}

export function withdraw(accountId: number, amount: number): Promise<Account> {
    return request<Account>(`${apiRoot}/${accountId}/withdraw`, {
        method: 'POST',
        body: JSON.stringify({ amount } satisfies AmountRequest),
    })
}

export function getTransactions(accountId: number): Promise<Transaction[]> {
    return request<Transaction[]>(`${apiRoot}/${accountId}/transactions`)
}
