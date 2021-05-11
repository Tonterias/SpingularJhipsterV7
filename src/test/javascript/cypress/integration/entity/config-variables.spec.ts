import {
  entityTableSelector,
  entityDetailsButtonSelector,
  entityDetailsBackButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityEditButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
} from '../../support/entity';

describe('ConfigVariables e2e test', () => {
  let startingEntitiesCount = 0;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.clearCookie('SESSION');
    cy.clearCookies();
    cy.intercept('GET', '/api/config-variables*').as('entitiesRequest');
    cy.visit('');
    cy.login('admin', 'admin');
    cy.clickOnEntityMenuItem('config-variables');
    cy.wait('@entitiesRequest').then(({ request, response }) => (startingEntitiesCount = response.body.length));
    cy.visit('/');
  });

  it('should load ConfigVariables', () => {
    cy.intercept('GET', '/api/config-variables*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('config-variables');
    cy.wait('@entitiesRequest');
    cy.getEntityHeading('ConfigVariables').should('exist');
    if (startingEntitiesCount === 0) {
      cy.get(entityTableSelector).should('not.exist');
    } else {
      cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount);
    }
    cy.visit('/');
  });

  it('should load details ConfigVariables page', () => {
    cy.intercept('GET', '/api/config-variables*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('config-variables');
    cy.wait('@entitiesRequest');
    if (startingEntitiesCount > 0) {
      cy.get(entityDetailsButtonSelector).first().click({ force: true });
      cy.getEntityDetailsHeading('configVariables');
      cy.get(entityDetailsBackButtonSelector).should('exist');
    }
    cy.visit('/');
  });

  it('should load create ConfigVariables page', () => {
    cy.intercept('GET', '/api/config-variables*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('config-variables');
    cy.wait('@entitiesRequest');
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('ConfigVariables');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.visit('/');
  });

  it('should load edit ConfigVariables page', () => {
    cy.intercept('GET', '/api/config-variables*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('config-variables');
    cy.wait('@entitiesRequest');
    if (startingEntitiesCount > 0) {
      cy.get(entityEditButtonSelector).first().click({ force: true });
      cy.getEntityCreateUpdateHeading('ConfigVariables');
      cy.get(entityCreateSaveButtonSelector).should('exist');
    }
    cy.visit('/');
  });

  it('should create an instance of ConfigVariables', () => {
    cy.intercept('GET', '/api/config-variables*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('config-variables');
    cy.wait('@entitiesRequest').then(({ request, response }) => (startingEntitiesCount = response.body.length));
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('ConfigVariables');

    cy.get(`[data-cy="configVarLong1"]`).type('88634').should('have.value', '88634');

    cy.get(`[data-cy="configVarLong2"]`).type('35590').should('have.value', '35590');

    cy.get(`[data-cy="configVarLong3"]`).type('2503').should('have.value', '2503');

    cy.get(`[data-cy="configVarLong4"]`).type('51211').should('have.value', '51211');

    cy.get(`[data-cy="configVarLong5"]`).type('84911').should('have.value', '84911');

    cy.get(`[data-cy="configVarLong6"]`).type('4453').should('have.value', '4453');

    cy.get(`[data-cy="configVarLong7"]`).type('11579').should('have.value', '11579');

    cy.get(`[data-cy="configVarLong8"]`).type('11260').should('have.value', '11260');

    cy.get(`[data-cy="configVarLong9"]`).type('9103').should('have.value', '9103');

    cy.get(`[data-cy="configVarLong10"]`).type('53681').should('have.value', '53681');

    cy.get(`[data-cy="configVarLong11"]`).type('55831').should('have.value', '55831');

    cy.get(`[data-cy="configVarLong12"]`).type('64910').should('have.value', '64910');

    cy.get(`[data-cy="configVarLong13"]`).type('36353').should('have.value', '36353');

    cy.get(`[data-cy="configVarLong14"]`).type('75133').should('have.value', '75133');

    cy.get(`[data-cy="configVarLong15"]`).type('10012').should('have.value', '10012');

    cy.get(`[data-cy="configVarBoolean16"]`).should('not.be.checked');
    cy.get(`[data-cy="configVarBoolean16"]`).click().should('be.checked');

    cy.get(`[data-cy="configVarBoolean17"]`).should('not.be.checked');
    cy.get(`[data-cy="configVarBoolean17"]`).click().should('be.checked');

    cy.get(`[data-cy="configVarBoolean18"]`).should('not.be.checked');
    cy.get(`[data-cy="configVarBoolean18"]`).click().should('be.checked');

    cy.get(`[data-cy="configVarString19"]`)
      .type('overriding Soap SCSI', { force: true })
      .invoke('val')
      .should('match', new RegExp('overriding Soap SCSI'));

    cy.get(`[data-cy="configVarString20"]`)
      .type('generate discrete', { force: true })
      .invoke('val')
      .should('match', new RegExp('generate discrete'));

    cy.get(entityCreateSaveButtonSelector).click({ force: true });
    cy.scrollTo('top', { ensureScrollable: false });
    cy.get(entityCreateSaveButtonSelector).should('not.exist');
    cy.intercept('GET', '/api/config-variables*').as('entitiesRequestAfterCreate');
    cy.visit('/');
    cy.clickOnEntityMenuItem('config-variables');
    cy.wait('@entitiesRequestAfterCreate');
    cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount + 1);
    cy.visit('/');
  });

  it('should delete last instance of ConfigVariables', () => {
    cy.intercept('GET', '/api/config-variables*').as('entitiesRequest');
    cy.intercept('DELETE', '/api/config-variables/*').as('deleteEntityRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('config-variables');
    cy.wait('@entitiesRequest').then(({ request, response }) => {
      startingEntitiesCount = response.body.length;
      if (startingEntitiesCount > 0) {
        cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount);
        cy.get(entityDeleteButtonSelector).last().click({ force: true });
        cy.getEntityDeleteDialogHeading('configVariables').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest');
        cy.intercept('GET', '/api/config-variables*').as('entitiesRequestAfterDelete');
        cy.visit('/');
        cy.clickOnEntityMenuItem('config-variables');
        cy.wait('@entitiesRequestAfterDelete');
        cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount - 1);
      }
      cy.visit('/');
    });
  });
});
