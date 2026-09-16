import {useParams} from 'react-router'

export function parseAccountId(value: string | undefined): number | null {
    if (!value || !/^\d+$/.test(value)) {
        return null
    }

    const accountId = Number(value)
    return Number.isSafeInteger(accountId) && accountId > 0 ? accountId : null
}

export function useAccountId(): { accountId: number | null; error: string | null } {
    const {accountId: rawAccountId} = useParams<{ accountId: string }>()
    const accountId = parseAccountId(rawAccountId)

    return {
        accountId,
        error: accountId === null ? 'Enter a valid positive account ID in the URL.' : null,
    }
}