interface QueryParams {
    [paramName: string]: string;
}

export function parseQueryParams(searchString: string): QueryParams {
    const paramString = searchString.split('?');
    const params = paramString[paramString.length - 1].split('&');
    const result: QueryParams = {};
    params.forEach(param => {
        const eqSignIndex = param.indexOf('=');
        const paramName = param.substring(0, eqSignIndex);
        result[paramName] = param.substring(eqSignIndex + 1, param.length);
    });
    return result;
}