import axios from 'axios'
import {API_BASE_PATH} from "../config.js";

const API_BASE_URL = `${API_BASE_PATH}/imports`

class ImportService {
    constructor() {
        this.api = axios.create({
            baseURL: API_BASE_URL,
            timeout: 30000, // Увеличен таймаут для импорта
            headers: {
                'Content-Type': 'multipart/form-data'
            }
        })
    }

    importMovies(fileData) {
        return this.api.post('', fileData)
    }
}

export default new ImportService()