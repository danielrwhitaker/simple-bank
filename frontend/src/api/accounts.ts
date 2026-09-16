export type Account = {
    id: number
    type: string
    balance: number | string
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

const apiRoot = '/api/accounts'

async function request<T>(url: string, options?: RequestInit): Promise<T> {
    const response = await fetch(url, {
        headers: {
            'Content-Type': 'application/json',
            ...options?.headers,
        },
        ...options,
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