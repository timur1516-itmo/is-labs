import axios from 'axios'
import {API_BASE_PATH} from "../config.js";

const API_BASE_URL = `${API_BASE_PATH}/imports`

class ImportOperationService {
    constructor() {
        this.api = axios.create({
            baseURL: API_BASE_URL,
            timeout: 10000,
            headers: {
                'Content-Type': 'application/json'
            }
        })
    }

    getImportOperations(lazyState) {
        const params = new URLSearchParams();

        params.append('first', lazyState.first);
        params.append('pageSize', lazyState.rows);
        if (lazyState.sortField) {
            params.append('sortField', lazyState.sortField);
            params.append('sortOrder', lazyState.sortOrder);
        }
        if (lazyState.filters) {
            for (const [field, filter] of Object.entries(lazyState.filters)) {
                if (filter.value) {
                    params.append(field, filter.value);
                }
            }
        }
        return this.api.get('/', {params})
    }
}

export default new ImportOperationService()