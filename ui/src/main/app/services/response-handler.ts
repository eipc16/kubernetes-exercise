// eslint-disable-next-line @typescript-eslint/no-explicit-any
export function handleResponse(response: any): any {
    return response.text()
        .then((text: string) => {
            const data = text && JSON.parse(text);
            if (!response.ok) {
                const error = (data && data.message) || response.statusText;
                return Promise.reject(error)
            }
            return data
        })
}
