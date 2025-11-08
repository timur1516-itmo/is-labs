import React, {useEffect, useRef, useState} from 'react'
import {Card} from 'primereact/card'
import {Toast} from 'primereact/toast'
import {Dialog} from 'primereact/dialog'
import ImportOperationService from "../../services/ImportOperationService.jsx";
import ImportOperationTable from "../../components/import/ImportOperationTable.jsx";

const ImportOperationListPage = () => {
    const [importOperations, setImportOperations] = useState([])
    const [loading, setLoading] = useState(false)
    const [totalRecords, setTotalRecords] = useState(100)
    const [lazyState, setLazyState] = useState({
        first: 0,
        rows: 10,
        sortField: null,
        sortOrder: null,
        filters: {}
    });
    const [selectedImportOperation, setSelectedImportOperation] = useState(null)
    const [viewDialogVisible, setViewDialogVisible] = useState(false)
    const toast = useRef(null)

    useEffect(() => {
        loadImportOperations()
    }, [lazyState])

    const loadImportOperations = async () => {
        setLoading(true)
        try {
            const response = await ImportOperationService.getImportOperations(lazyState)
            setImportOperations(response.data.data)
            setTotalRecords(response.data.totalRecords)
        } catch (error) {
            toast.current.show({severity: 'error', summary: 'Ошибка', detail: 'Не удалось загрузить операции импорта'})
        } finally {
            setLoading(false)
        }
    }

    const handleView = () => {
        if (selectedImportOperation) {
            setViewDialogVisible(true)
        }
    }

    const handleSelectionChange = (e) => {
        setSelectedImportOperation(e.value)
    }

    const handleLazyStateChange = (e) => {
        setLazyState(e);
    }

    const formatDateTime = (dateString) => {
        if (!dateString) return '';
        const date = new Date(dateString);
        return date.toLocaleString('ru-RU');
    };

    return (
        <div>
            <Toast ref={toast}/>

            {/* Диалог просмотра */}
            <Dialog
                header="Детали операции импорта"
                visible={viewDialogVisible}
                style={{width: '50vw'}}
                onHide={() => setViewDialogVisible(false)}
            >
                {selectedImportOperation && (
                    <div>
                        <div className="mb-2"><strong>ID:</strong> {selectedImportOperation.id}</div>
                        <div className="mb-2"><strong>Статус:</strong> {selectedImportOperation.status}</div>
                        <div className="mb-2"><strong>Дата начала:</strong> {formatDateTime(selectedImportOperation.startDt)}</div>
                        <div className="mb-2"><strong>Дата окончания:</strong> {formatDateTime(selectedImportOperation.endDt)}</div>
                        <div className="mb-2"><strong>Количество импортировано:</strong> {selectedImportOperation.importedCnt}</div>
                        {selectedImportOperation.errorMessage && (
                            <div className="mb-2">
                                <strong>Ошибка:</strong> {selectedImportOperation.errorMessage}
                            </div>
                        )}
                    </div>
                )}
            </Dialog>

            <Card title="Операции импорта">
                <ImportOperationTable
                    importOperations={importOperations}
                    loading={loading}
                    lazyState={lazyState}
                    setLazyState={setLazyState}
                    totalRecords={totalRecords}
                    selection={selectedImportOperation}
                    onSelectionChange={handleSelectionChange}
                />
            </Card>
        </div>
    )
}

export default ImportOperationListPage