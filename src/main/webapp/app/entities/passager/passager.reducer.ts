import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IPassager, defaultValue } from 'app/shared/model/passager.model';

export const ACTION_TYPES = {
  SEARCH_PASSAGERS: 'passager/SEARCH_PASSAGERS',
  FETCH_PASSAGER_LIST: 'passager/FETCH_PASSAGER_LIST',
  FETCH_PASSAGER: 'passager/FETCH_PASSAGER',
  CREATE_PASSAGER: 'passager/CREATE_PASSAGER',
  UPDATE_PASSAGER: 'passager/UPDATE_PASSAGER',
  DELETE_PASSAGER: 'passager/DELETE_PASSAGER',
  RESET: 'passager/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IPassager>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type PassagerState = Readonly<typeof initialState>;

// Reducer

export default (state: PassagerState = initialState, action): PassagerState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_PASSAGERS):
    case REQUEST(ACTION_TYPES.FETCH_PASSAGER_LIST):
    case REQUEST(ACTION_TYPES.FETCH_PASSAGER):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_PASSAGER):
    case REQUEST(ACTION_TYPES.UPDATE_PASSAGER):
    case REQUEST(ACTION_TYPES.DELETE_PASSAGER):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.SEARCH_PASSAGERS):
    case FAILURE(ACTION_TYPES.FETCH_PASSAGER_LIST):
    case FAILURE(ACTION_TYPES.FETCH_PASSAGER):
    case FAILURE(ACTION_TYPES.CREATE_PASSAGER):
    case FAILURE(ACTION_TYPES.UPDATE_PASSAGER):
    case FAILURE(ACTION_TYPES.DELETE_PASSAGER):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.SEARCH_PASSAGERS):
    case SUCCESS(ACTION_TYPES.FETCH_PASSAGER_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_PASSAGER):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_PASSAGER):
    case SUCCESS(ACTION_TYPES.UPDATE_PASSAGER):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_PASSAGER):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {}
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState
      };
    default:
      return state;
  }
};

const apiUrl = 'api/passagers';
const apiSearchUrl = 'api/_search/passagers';

// Actions

export const getSearchEntities: ICrudSearchAction<IPassager> = (query, page, size, sort) => ({
  type: ACTION_TYPES.SEARCH_PASSAGERS,
  payload: axios.get<IPassager>(`${apiSearchUrl}?query=${query}`)
});

export const getEntities: ICrudGetAllAction<IPassager> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_PASSAGER_LIST,
  payload: axios.get<IPassager>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IPassager> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_PASSAGER,
    payload: axios.get<IPassager>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IPassager> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_PASSAGER,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IPassager> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_PASSAGER,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IPassager> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_PASSAGER,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
