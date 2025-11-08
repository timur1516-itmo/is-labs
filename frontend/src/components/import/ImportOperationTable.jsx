import React, {useEffect, useRef} from 'react'
import {DataTable} from 'primereact/datatable'
import {Column} from 'primereact/column'
import {Dropdown} from "primereact/dropdown";
import {IMPORT_OPERATION_STATUSES} from "../../domain/values.js";
import {format, parseISO} from "date-fns";
import {ru} from "date-fns/locale";
import {WS_BASE_PATH} from "../../config.js";

const ImportOperationTable = ({
                                  importOperations,
                                  loading,
                                  lazyState,
                                  setLazyState,
                                  totalRecords,
                                  selection,
                                  onSelectionChange
                              }) => {

    const ws = useRef(null);

    useEffect(() => {
        ws.current = new WebSocket(`${WS_BASE_PATH}`);

        ws.current.onopen = () => {
            console.log('WebSocket connected for import operation');
        };

        ws.current.onmessage = (event) => {
            const data = JSON.parse(event.data);
            if (data.type === 'IMPORT_OPERATION') {
                setLazyState(prev => ({...prev}));
            }
        };

        ws.current.onerror = (error) => {
            console.error('WebSocket error:', error);
        };

        ws.current.onclose = () => {
            console.log('WebSocket disconnected for import operation');
        };

        return () => {
            if (ws.current) {
                ws.current.close();
            }
        };
    }, []);

    const onPage = (event) => {
        setLazyState(prev => ({
            ...prev,
            first: event.first,
            rows: event.rows,
            page: event.page
        }));
    };

    const onSort = (event) => {
        setLazyState(prev => ({
            ...prev,
            sortField: event.sortField,
            sortOrder: event.sortOrder
        }));
    };

    const onFilter = (event) => {
        event['first'] = 0;
        setLazyState(prev => ({
            ...prev,
            filters: event.filters,
            first: 0,
            page: 0
        }));
    };

    const statusFilterElement = (options) => {
        return (
            <Dropdown
                value={options.value}
                options={IMPORT_OPERATION_STATUSES}
                onChange={(e) => options.filterApplyCallback(e.value)}
                placeholder="Все"
                className="p-column-filter"
                showClear
            />
        );
    };

    const formatDate = (dateString) => {
        if (!dateString) return 'N/A';

        try {
            const date = parseISO(dateString);
            return format(date, 'dd.MM.yyyy HH:mm', {locale: ru});
        } catch (error) {
            console.error('Ошибка парсинга даты:', error);
            return dateString;
        }
    };

    const statusBodyTemplate = (rowData) => {
        return (
            <span className={`status-badge status-${rowData.status.toLowerCase()}`}>
                {rowData.status}
            </span>
        );
    };

    const startDtBodyTemplate = (rowData) => {
        return formatDate(rowData.startDt);
    };

    const endDtBodyTemplate = (rowData) => {
        return formatDate(rowData.endDt);
    };

    return (
        <div>
            <DataTable
                value={importOperations}
                lazy
                dataKey="id"
                first={lazyState.first}
                sortField={lazyState.sortField}
                sortOrder={lazyState.sortOrder}
                onFilter={onFilter}
                onPage={onPage}
                onSort={onSort}
                loading={loading}
                removableSort
                paginator
                rows={lazyState.rows}
                rowsPerPageOptions={[5, 10, 25, 50]}
                totalRecords={totalRecords}
                filterDisplay="row"
                emptyMessage="Операции импорта не найдены"
                selectionMode="single"
                selection={selection}
                onSelectionChange={onSelectionChange}
            >
                <Column
                    field="id"
                    header="ID"
                    sortable
                    filter
                    filterMatchMode="contains"
                    showFilterMenu={false}
                    showClearButton={false}
                />
                <Column
                    field="status"
                    header="Статус"
                    body={statusBodyTemplate}
                    sortable
                    filterElement={statusFilterElement}
                    filter
                    filterMatchMode="contains"
                    showFilterMenu={false}
                    showClearButton={false}
                />
                <Column
                    field="startDt"
                    header="Дата начала"
                    body={startDtBodyTemplate}
                    sortable
                />
                <Column
                    field="endDt"
                    header="Дата окончания"
                    body={endDtBodyTemplate}
                    sortable
                />
                <Column
                    field="importedCnt"
                    header="Количество импортировано"
                    sortable
                />
                <Column
                    field="errorMessage"
                    header="Сообщение об ошибке"
                    sortable
                />
            </DataTable>
        </div>
    )
}

export default ImportOperationTable